package com.example.carins.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record InsurancePolicyRequest(
        @NotBlank String provider,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {}