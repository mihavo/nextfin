package com.nextfin.messaging.transaction.service;

import com.twilio.Twilio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionConfirmationService {

    @Value("${clients.twilio.account-sid:#{null}}")
    private String accountSid;

    @Value("${clients.twilio.auth-token:#{null}}")
    private String authToken;

    @Value("${clients.twilio.phone-number:#{null}}")
    private String TWILIO_PHONE_NUMBER;

    private final MessageSource messageSource;

    @EventListener(ApplicationReadyEvent.class)
    private void initializeTwilio() {
        Twilio.init(accountSid, authToken);
    }

}
