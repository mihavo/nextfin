package com.michaelvol.bankingapp.messaging.sms.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSMSService {

    @Value("${clients.twilio.account-sid}")
    private String accountSid;

    @Value("${clients.twilio.auth-token}")
    private String authToken;

    @Value("${clients.twilio.phone-number}")
    private String TWILIO_PHONE_NUMBER;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeTwilio() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSMS(String to, String message) {
        Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                message
        ).create();
        System.out.println("SMS sent to " + to + " with message: " + message);
    }
}
