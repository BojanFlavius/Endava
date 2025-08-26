package com.example.carins.web.dto;

import com.example.carins.model.Car;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record InsuranceClaimRequest(
        @NotNull LocalDate claimDate,
        @NotBlank String description,
        @Positive @NotNull Long amount
) {}
