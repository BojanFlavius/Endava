package com.example.carins.web;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.service.InsurancePolicyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policies")
public class InsurancePolicyController {

    private final InsurancePolicyService policyService;

    public InsurancePolicyController(InsurancePolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping("/car/{carId}")
    public ResponseEntity<InsurancePolicy> createPolicy(
            @PathVariable Long carId,
            @Valid @RequestBody InsurancePolicy policy) {
        return ResponseEntity.ok(policyService.createPolicy(carId, policy));
    }

    @PutMapping("/{policyId}")
    public ResponseEntity<InsurancePolicy> updatePolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody InsurancePolicy updatedPolicy) {
        return ResponseEntity.ok(policyService.updatePolicy(policyId, updatedPolicy));
    }
}
