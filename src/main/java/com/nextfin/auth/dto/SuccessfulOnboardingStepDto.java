package com.nextfin.auth.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SuccessfulOnboardingStepDto<T> {
    OnboardingStatusDto onboardingStatus;
    T stepData;
}
