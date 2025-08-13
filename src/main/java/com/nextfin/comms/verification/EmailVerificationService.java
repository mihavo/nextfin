package com.nextfin.comms.verification;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailVerificationService {

    @Value("${clients.twilio.account-sid:#{null}}")
    private String accountSid;

    @Value("${clients.twilio.auth-token}")
    private String authToken;

    @Value("${clients.twilio.verification-sid}")
    private String verificationSid;

    private final MessageSource messageSource;

    @PostConstruct
    private void initializeTwilio() {
        Twilio.init(accountSid, authToken);
        log.info("2FA Initialized");
    }

    public Verification sendEmailVerificationCode(@Email String email) {
        Verification verification = Verification.creator(verificationSid, email, "email").create();
        log.info("OTP sent to {} for email verification, status: {}", email, verification.getStatus());
        return verification;
    }

    public void validateEmailVerificationCode(@Email String email, @Digits(integer = 6, fraction = 0, message =
            "The OTP Code " + "must be exactly 6 digits") String code) {
        VerificationCheck.creator(verificationSid).setTo(email).setCode(code).create();
    }
}
