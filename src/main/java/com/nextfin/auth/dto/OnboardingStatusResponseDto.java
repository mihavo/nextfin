package com.nextfin.auth.dto;

import com.nextfin.auth.enums.OnboardingStep;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OnboardingStatusResponseDto {
    boolean onboardingComplete;
    OnboardingStep stage;
}
