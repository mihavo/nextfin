package com.michaelvol.bankingapp.transaction.service.security;

import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import com.michaelvol.bankingapp.transaction.repository.TransactionRepository;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "2fa.enabled", havingValue = "true", matchIfMissing = true)
public class TransactionSecurityService {
    private final TransactionRepository transactionRepository;

    @Value("${clients.twilio.account-sid}")
    private String accountSid;

    @Value("${clients.twilio.auth-token}")
    private String authToken;

    @Value("${clients.twilio.verification-sid}")
    private String verificationSid;

    private final MessageSource messageSource;
    
    private void initializeTwilio() {
        Twilio.init(accountSid, authToken);
        log.info("2FA Initialized");
    }

    public Verification sendOTP(Transaction transaction) {
        String sourcePhone = transaction.getSourceAccount().getHolder().getUser().getPreferredPhoneNumber();
        Verification verification = Verification.creator(verificationSid, sourcePhone, "sms").create();
        log.info("OTP sent to {} for transaction {}, status: {}",
                 sourcePhone,
                 transaction.getId(),
                 verification.getStatus());
        transaction.setTransactionStatus(TransactionStatus.OTP_SENT);
        transactionRepository.save(transaction);
        return verification;
    }

    public void validateOTP(@Size(min = 10, max = 15, message = "Phone number should be between 10 and 15 digits") String sourcePhone,
                            @Digits(integer = 6, fraction = 0, message = "The OTP Code must be exactly 6 digits") String code) {
        VerificationCheck.creator(verificationSid).setTo(sourcePhone).setCode(code).create();
    }

}
