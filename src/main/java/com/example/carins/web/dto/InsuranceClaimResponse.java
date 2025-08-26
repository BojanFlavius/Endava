package com.example.carins.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InsuranceClaimResponse(
        Long id,
        LocalDate claimDate,
        String description,
        Long amount,
        Long carId
) {}