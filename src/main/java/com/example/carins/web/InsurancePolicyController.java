package com.example.carins.web;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.service.InsurancePolicyService;
import com.example.carins.web.dto.InsurancePolicyRequest;
import com.example.carins.web.dto.InsurancePolicyResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class InsurancePolicyController {

    private final InsurancePolicyService policyService;

    public InsurancePolicyController(InsurancePolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping("/car/{carId}")
    public List<InsurancePolicyResponse> getPoliciesForCar(@PathVariable Long carId) {
        return policyService.getPoliciesForCar(carId);
    }

    @PostMapping("/car/{carId}")
    public ResponseEntity<InsurancePolicyResponse> createPolicy(
            @PathVariable Long carId,
            @Valid @RequestBody InsurancePolicyRequest request) {
        InsurancePolicyResponse response = policyService.createPolicy(carId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{policyId}")
    public ResponseEntity<InsurancePolicyResponse> updatePolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody InsurancePolicyRequest request) {

        InsurancePolicyResponse response = policyService.updatePolicy(policyId, request);
        return ResponseEntity.ok(response);
    }
}
