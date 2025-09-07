package com.example.carins;

import com.example.carins.service.CarService;
import com.example.carins.web.dto.CarHistoryEvent;
import com.example.carins.web.dto.InsuranceClaimRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarInsuranceApplicationTests {

    @Autowired
    private CarService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void insuranceValidityBasic() {
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2024-06-01")));
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2025-06-01")));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(2L, LocalDate.parse("2025-02-01")));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Insurance not valid on this date", ex.getReason());
    }

    @Test
    void insuranceValidity_existingCar_validDateCovered(){
        assertTrue(service.isInsuranceValid(1L,LocalDate.parse("2024-06-01")));
    }

    @Test
    void insuranceValidity_existingCar_validDateNotCovered(){
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.isInsuranceValid(2L, LocalDate.parse("2025-02-01")));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Insurance not valid on this date", ex.getReason());
    }

    @Test
    void insuranceValidity_nonExistingCar_shouldThrow404(){
        assertThrows(ResponseStatusException.class,() -> service.isInsuranceValid(99L, LocalDate.parse("2024-06-01")));
    }

    @Test
    void createClaim_futureDate_shouldThrow400() {
        var request = new InsuranceClaimRequest(
                LocalDate.now().plusDays(5),
                "Future accident",
                1000L
        );

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.createClaimForCar(1L, request));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Claim date cannot be in the future", ex.getReason());
    }

    @Test
    void createClaim_nonExistingCar_shouldThrow404() {
        var request = new InsuranceClaimRequest(
                LocalDate.parse("2024-05-15"),
                "Accident on missing car",
                700L
        );

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.createClaimForCar(999L, request));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Car not found with id 999", ex.getReason());
    }

    @Test
    void getClaimsForCar_shouldReturnClaimsSortedByDateDesc() {
        List<CarHistoryEvent> events = service.getClaimsForCar(1L);

        assertFalse(events.isEmpty());
        assertTrue(events.get(0).claimDate().isAfter(events.get(events.size()-1).claimDate())
                || events.get(0).claimDate().isEqual(events.get(events.size()-1).claimDate()));
    }
    @Test
    void controller_shouldReturnCarHistory() throws Exception {
        mockMvc.perform(get("/api/cars/1/history"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("description")));
    }

    @Test
    void controller_claimInsurance_invalidFutureDate_shouldFail() throws Exception {
        String requestJson = """
        {
          "claimDate": "2099-01-01",
          "description": "Future claim",
          "amount": 1000.0
        }
        """;

        mockMvc.perform(
                        post("/api/cars/1/claims")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void controller_claimInsurance_nonExistingCar_shouldFail() throws Exception {
        String requestJson = """
        {
          "claimDate": "2024-05-10",
          "description": "Claim for missing car",
          "amount": 500.0
        }
        """;

        mockMvc.perform(
                        post("/api/cars/999/claims")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                .andExpect(status().isNotFound());
    }
}
