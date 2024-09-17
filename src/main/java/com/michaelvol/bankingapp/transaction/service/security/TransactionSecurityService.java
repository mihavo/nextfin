package com.michaelvol.bankingapp.transaction.service.security;

import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionSecurityService {

    @Value("${clients.twilio.account-sid}")
    private String accountSid;

    @Value("${clients.twilio.auth-token}")
    private String authToken;

    @Value("${clients.twilio.verification-sid}")
    private String verificationSid;

    private final MessageSource messageSource;

    @EventListener(ApplicationReadyEvent.class)
    private void initializeTwilio() {
        Twilio.init(accountSid, authToken);
    }

    public void authenticateTransaction(Transaction transaction) {
        generateOTP(transaction);
    }

    private void generateOTP(Transaction transaction) {
        String recipient = transaction.getSourceAccount().getHolder().getUser().getPreferredPhoneNumber();
        Verification verification = Verification.creator(verificationSid, recipient, "sms").create();
        log.info("(OTP sent to {} for transaction {}, status: {}",
                 recipient,
                 transaction.getId(),
                 verification.getStatus());
    }


}
