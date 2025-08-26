package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsuranceClaimRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.CarHistoryEvent;
import com.example.carins.web.dto.InsuranceClaimRequest;
import com.example.carins.web.dto.InsuranceClaimResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final InsurancePolicyRepository policyRepository;
    private final InsuranceClaimRepository insuranceClaimRepository;

    public CarService(CarRepository carRepository, InsurancePolicyRepository policyRepository, InsuranceClaimRepository insuranceClaimRepository) {
        this.carRepository = carRepository;
        this.policyRepository = policyRepository;
        this.insuranceClaimRepository = insuranceClaimRepository;
    }

    public List<Car> listCars() {
        return carRepository.findAll();
    }

    public boolean isInsuranceValid(Long carId, LocalDate date) {
        if (carId == null || date == null) return false;
        // TODO: optionally throw NotFound if car does not exist
        if(!carRepository.existsById(carId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Car not found with id " + carId);
        }
        List<InsurancePolicy> policies = policyRepository.findByCarId(carId);
        Optional<InsurancePolicy> activePolicy = policies.stream().filter(p->!date.isBefore(p.getStartDate()) && !date.isAfter(p.getEndDate())).findFirst();

        if(activePolicy.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Insurance not valid on this date");
        }
        return policyRepository.existsActiveOnDate(carId, date);
    }

    public InsuranceClaimResponse createClaimForCar(Long carId, @Valid InsuranceClaimRequest claimRequest) {
        Car car = carRepository.findById(carId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found with id " + carId));
        InsuranceClaim claim = new InsuranceClaim();

        claim.setCar(car);
        claim.setClaimDate(claimRequest.claimDate());
        claim.setDescription(claimRequest.description());
        claim.setAmount(claimRequest.amount());

        InsuranceClaim savedClaim = insuranceClaimRepository.save(claim);

        return new InsuranceClaimResponse(savedClaim.getId(),savedClaim.getClaimDate(),savedClaim.getDescription(), savedClaim.getAmount(), car.getId());
    }

    public List<CarHistoryEvent> getClaimsForCar(Long carId) {
        if(!carRepository.existsById(carId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found with id " + carId);
        }
        List<InsuranceClaim> claims = insuranceClaimRepository.findByCarId(carId);

        return claims.stream().sorted(Comparator.comparing(InsuranceClaim::getClaimDate).reversed())
                .map(c -> new CarHistoryEvent(c.getClaimDate(),c.getDescription(),c.getAmount()))
                .toList();
    }
}
