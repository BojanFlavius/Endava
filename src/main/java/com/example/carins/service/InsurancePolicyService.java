package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.web.dto.InsurancePolicyRequest;
import com.example.carins.web.dto.InsurancePolicyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class InsurancePolicyService {

    private final InsurancePolicyRepository policyRepository;
    private final CarRepository carRepository;

    public InsurancePolicyService(InsurancePolicyRepository policyRepository, CarRepository carRepository) {
        this.policyRepository = policyRepository;
        this.carRepository = carRepository;
    }

    public List<InsurancePolicyResponse> getPoliciesForCar(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found with id " + carId);
        }

        return policyRepository.findByCarId(carId).stream()
                .map(policy -> new InsurancePolicyResponse(
                        policy.getId(),
                        policy.getProvider(),
                        policy.getStartDate(),
                        policy.getEndDate(),
                        carId
                ))
                .toList();
    }

    public InsurancePolicyResponse createPolicy(Long carId, InsurancePolicyRequest request) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found with id " + carId));

        if (request.startDate().isAfter(request.endDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date cannot be after end date");
        }

        InsurancePolicy policy = new InsurancePolicy();
        policy.setCar(car);
        policy.setProvider(request.provider());
        policy.setStartDate(request.startDate());
        policy.setEndDate(request.endDate());

        InsurancePolicy saved = policyRepository.save(policy);

        return new InsurancePolicyResponse(
                saved.getId(),
                saved.getProvider(),
                saved.getStartDate(),
                saved.getEndDate(),
                car.getId()
        );
    }

    public InsurancePolicyResponse updatePolicy(Long policyId, InsurancePolicyRequest request) {
        InsurancePolicy existing = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Policy not found with id " + policyId));

        existing.setProvider(request.provider());
        existing.setStartDate(request.startDate());
        existing.setEndDate(request.endDate());

        InsurancePolicy saved = policyRepository.save(existing);

        return new InsurancePolicyResponse(
                saved.getId(),
                saved.getProvider(),
                saved.getStartDate(),
                saved.getEndDate(),
                saved.getCar().getId()
        );
    }
}
