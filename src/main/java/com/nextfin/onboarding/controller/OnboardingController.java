package com.nextfin.onboarding.controller;

import com.nextfin.AppConstants;
import com.nextfin.auth.dto.OnboardingStatusDto;
import com.nextfin.auth.dto.SuccessfulOnboardingStepDto;
import com.nextfin.auth.enums.OnboardingStep;
import com.nextfin.holder.dto.CreateHolderDto;
import com.nextfin.holder.entity.Holder;
import com.nextfin.onboarding.entity.Tos;
import com.nextfin.onboarding.entity.TosAcceptance;
import com.nextfin.onboarding.service.OnboardingService;
import com.twilio.rest.verify.v2.service.Verification;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/onboarding")
@RequiredArgsConstructor
@Tag(name = "Onboarding", description = "Endpoints for the onboarding process")
public class OnboardingController {

    private final OnboardingService onboardingService;
    private final MessageSource messageSource;

    @GetMapping("/status")
    public ResponseEntity<OnboardingStatusDto> getStatus() {
        OnboardingStep currentStep = onboardingService.getCurrentUserOnboardingStep();
        boolean isOnboardingComplete = currentStep.equals(OnboardingStep.COMPLETED);
        return new ResponseEntity<>(OnboardingStatusDto.builder().onboardingComplete(isOnboardingComplete).step(currentStep)
                                                       .build(), HttpStatus.OK);
    }

    @PostMapping("/create-holder")
    public ResponseEntity<SuccessfulOnboardingStepDto<Holder>> createHolder(@Valid @RequestBody CreateHolderDto request) {
        Holder holder = onboardingService.createHolder(request);
        OnboardingStatusDto status = OnboardingStatusDto.builder().onboardingComplete(false).step(
                OnboardingStep.HOLDER_CREATION.next()).build();
        SuccessfulOnboardingStepDto<Holder> response = SuccessfulOnboardingStepDto.<Holder>builder().onboardingStatus(status)
                                                                                  .stepData(holder).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/accept-terms")
    public ResponseEntity<SuccessfulOnboardingStepDto<TosAcceptance>> acceptTerms() {
        TosAcceptance acceptance = onboardingService.acceptTerms();
        OnboardingStatusDto status = OnboardingStatusDto.builder().onboardingComplete(false).step(
                OnboardingStep.TOS_ACCEPTANCE.next()).build();
        SuccessfulOnboardingStepDto<TosAcceptance> response = SuccessfulOnboardingStepDto.<TosAcceptance>builder()
                                                                                         .onboardingStatus(status).stepData(
                        acceptance).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Verification> verifyEmail() {
        Verification verification = onboardingService.verifyEmail();
        return new ResponseEntity<>(verification, HttpStatus.OK);
    }

    @PostMapping("/validate-email-otp")
    public ResponseEntity<SuccessfulOnboardingStepDto<String>> validateEmailOtp(@RequestBody String code) {
        onboardingService.validateEmailOtp(code);
        OnboardingStatusDto status = OnboardingStatusDto.builder().onboardingComplete(true).step(
                OnboardingStep.HOLDER_CREATION.next()).build();
        String emailVerifiedMessage = messageSource.getMessage("onboarding.email.verified", null,
                                                               LocaleContextHolder.getLocale());
        SuccessfulOnboardingStepDto<String> response = SuccessfulOnboardingStepDto.<String>builder().onboardingStatus(status)
                                                                                  .stepData(emailVerifiedMessage).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/terms")
    public ResponseEntity<Tos> getTermsOfService() {
        Tos tos = onboardingService.getTermsOfService();
        return new ResponseEntity<>(tos, HttpStatus.OK);
    }
}
