package com.example.carins;

import com.example.carins.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
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
    void controller_shouldRejectInvalidDateFormat() throws Exception {
        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "2025/02/01"))  // wrong format
                .andExpect(status().isBadRequest());
    }

    @Test
    void controller_shouldRejectImpossibleDate() throws Exception {
        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "2025-02-30"))  // impossible date
                .andExpect(status().isBadRequest());
    }
}
