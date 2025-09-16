package com.example.carins.web.dto;

import java.time.LocalDate;

public record InsurancePolicyResponse(
        Long id,
        String provider,
        LocalDate startDate,
        LocalDate endDate,
        Long carId
) {}