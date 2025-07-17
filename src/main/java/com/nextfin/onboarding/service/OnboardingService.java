package com.nextfin.onboarding.service;

import com.nextfin.AppConstants;
import com.nextfin.auth.enums.OnboardingStep;
import com.nextfin.holder.dto.CreateHolderDto;
import com.nextfin.holder.entity.Holder;
import com.nextfin.holder.service.HolderService;
import com.nextfin.onboarding.entity.TosAcceptance;
import com.nextfin.onboarding.repository.TosAcceptanceRepository;
import com.nextfin.users.entity.User;
import com.nextfin.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserService userService;
    private final HolderService holderService;

    private final TosAcceptanceRepository tosAcceptanceRepository;

    public OnboardingStep getCurrentUserOnboardingStep() {
        User user = userService.getCurrentUser();
        return user.getOnboardingStep();
    }

    public Holder createHolder(@Valid CreateHolderDto request) {
        User user = userService.getCurrentUser();
        OnboardingStep currentStep = user.getOnboardingStep();
        if (!currentStep.equals(OnboardingStep.HOLDER_CREATION)) {
            throw new IllegalStateException("Cannot create holder at this step: " + currentStep);
        }
        Holder holder = holderService.createHolder(request, user);
        user.setOnboardingStep(currentStep.next());
        return holder;
    }

    public TosAcceptance acceptTerms() {
        User user = userService.getCurrentUser();
        OnboardingStep currentStep = user.getOnboardingStep();
        if (!currentStep.equals(OnboardingStep.TOS_ACCEPTED)) {
            throw new IllegalStateException("Cannot accept terms at this step: " + currentStep);
        }
        TosAcceptance acceptance = TosAcceptance.builder().acceptanceDate(LocalDateTime.now()).tosVersion(
                AppConstants.LATEST_TOS_VERSION).build();
        return tosAcceptanceRepository.save(acceptance);
    }
}
