package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.service.CarService;
import com.example.carins.web.dto.CarDto;
import com.example.carins.web.dto.CarHistoryEvent;
import com.example.carins.web.dto.InsuranceClaimRequest;
import com.example.carins.web.dto.InsuranceClaimResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;

    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<InsuranceValidityResponse> isInsuranceValid(
            @PathVariable Long carId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        boolean valid = service.isInsuranceValid(carId, date);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, date.toString(), valid));
    }

    @PostMapping("/cars/{carId}/claims")
    public ResponseEntity<InsuranceClaimResponse> claimInsurance(
            @PathVariable Long carId,
            @RequestBody @Valid InsuranceClaimRequest claimRequest) {

        InsuranceClaimResponse response = service.createClaimForCar(carId, claimRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cars/{carId}/history")
    public ResponseEntity<List<CarHistoryEvent>> carHistory(@PathVariable Long carId) {
        List<CarHistoryEvent> claims = service.getClaimsForCar(carId);
        return ResponseEntity.ok(claims);
    }

    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(
                c.getId(),
                c.getVin(),
                c.getMake(),
                c.getModel(),
                c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null
        );
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {}
}
