package com.michaelvol.bankingapp.messaging.transaction.service;

import com.michaelvol.bankingapp.transaction.entity.Transaction;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class TransactionConfirmationService {

    @Value("${clients.twilio.account-sid}")
    private String accountSid;

    @Value("${clients.twilio.auth-token}")
    private String authToken;

    @Value("${clients.twilio.phone-number}")
    private String TWILIO_PHONE_NUMBER;

    private final MessageSource messageSource;

    @EventListener(ApplicationReadyEvent.class)
    private void initializeTwilio() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSMS(String to, Transaction transaction) {
        ZonedDateTime zonedTimestamp = transaction.getUpdatedAt().atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss");
        String formattedTimestamp = zonedTimestamp.format(formatter);

        String message = messageSource.getMessage("transaction.transfer.sms-confirmation",
                                                  new String[]{
                                                          formattedTimestamp,
                                                          transaction.getAmount().toString(),
                                                          transaction.getCurrency().getCurrencyCode(),
                                                          transaction.getTargetAccount().getHolder().getFullName()
                                                  },
                                                  LocaleContextHolder.getLocale());
        Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                message
        ).create();
    }
}
