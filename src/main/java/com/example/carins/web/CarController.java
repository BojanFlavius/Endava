package com.example.carins.web;

import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.repo.CarRepository;
import com.example.carins.service.CarService;
import com.example.carins.web.dto.CarDto;
import com.example.carins.web.dto.CarHistoryEvent;
import com.example.carins.web.dto.InsuranceClaimRequest;
import com.example.carins.web.dto.InsuranceClaimResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService service;

    public CarController(CarService service){
        this.service = service;
    }

    @GetMapping("/cars")
    public List<CarDto> getCars() {
        return service.listCars().stream().map(this::toDto).toList();
    }

    @GetMapping("/cars/{carId}/insurance-valid")
    public ResponseEntity<?> isInsuranceValid(@PathVariable Long carId, @RequestParam String date) {
        // TODO: validate date format and handle errors consistently
        LocalDate d = LocalDate.parse(date);
        boolean valid = service.isInsuranceValid(carId, d);
        return ResponseEntity.ok(new InsuranceValidityResponse(carId, d.toString(), valid));
    }

    @PostMapping("/cars/{carId}/claims")
    public ResponseEntity<?> claimInsurance(@PathVariable Long carId, @Valid @RequestBody InsuranceClaimRequest claimRequest){
        InsuranceClaimResponse response = service.createClaimForCar(carId, claimRequest);

        URI location = URI.create("/cars/" + carId + "/claims/" + response.id());

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/cars/{carId}/history")
    public ResponseEntity<?> carHistory(@PathVariable Long carId){
        List<CarHistoryEvent> claims = service.getClaimsForCar(carId);
        return ResponseEntity.ok(claims);
    }

    private CarDto toDto(Car c) {
        var o = c.getOwner();
        return new CarDto(c.getId(), c.getVin(), c.getMake(), c.getModel(), c.getYearOfManufacture(),
                o != null ? o.getId() : null,
                o != null ? o.getName() : null,
                o != null ? o.getEmail() : null);
    }

    public record InsuranceValidityResponse(Long carId, String date, boolean valid) {}
}
