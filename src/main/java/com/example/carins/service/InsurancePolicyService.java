package com.example.carins.service;

import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class InsurancePolicyService {

    private final InsurancePolicyRepository policyRepository;
    private final CarRepository carRepository;

    public InsurancePolicyService(InsurancePolicyRepository policyRepository, CarRepository carRepository) {
        this.policyRepository = policyRepository;
        this.carRepository = carRepository;
    }

    public InsurancePolicy createPolicy(Long carId, InsurancePolicy policy) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found with id " + carId));

        policy.setCar(car);
        return policyRepository.save(policy);
    }

    public InsurancePolicy updatePolicy(Long policyId, InsurancePolicy updatedPolicy) {
        InsurancePolicy existing = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Policy not found with id " + policyId));

        existing.setProvider(updatedPolicy.getProvider());
        existing.setStartDate(updatedPolicy.getStartDate());
        existing.setEndDate(updatedPolicy.getEndDate());

        return policyRepository.save(existing);
    }
}
