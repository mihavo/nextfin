package com.nextfin.transaction.service.sms;

import com.nextfin.account.entity.Account;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.service.confirmation.ConfirmationService;
import com.nextfin.transaction.service.utils.TransactionUtils;
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
public class SMSService implements ConfirmationService {

    @Value("${clients.twilio.account-sid:#{null}}")
    private String accountSid;

    @Value("${clients.twilio.auth-token:#{null}}")
    private String authToken;

    @Value("${clients.twilio.phone-number:#{null}}")
    private String TWILIO_PHONE_NUMBER;

    private final MessageSource messageSource;

    private final TransactionUtils transactionUtils;

    @EventListener(ApplicationReadyEvent.class)
    private void initializeTwilio() {
        Twilio.init(accountSid, authToken);
    }


    @Override
    public void handleConfirmation(Account sourceAccount, Transaction processedTransaction) {
        if (transactionUtils.check2fa(sourceAccount)) {
            String initiator = sourceAccount.getHolder().getUser().getPreferredPhoneNumber();
            sendSMS(initiator, processedTransaction);
        }
    }


    private void sendSMS(String to, Transaction transaction) {
        ZonedDateTime zonedTimestamp = transaction.getUpdatedAt().atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss");
        String formattedTimestamp = zonedTimestamp.format(formatter);

        String message = messageSource.getMessage("transaction.transfer.sms-confirmation",
                                                  new String[]{formattedTimestamp, transaction.getAmount().toString(),
                                                               transaction.getCurrency().getCurrencyCode(),
                                                               transaction.getTargetAccount().getHolder().getFullName()},
                                                  LocaleContextHolder.getLocale());
        Message.creator(new com.twilio.type.PhoneNumber(to), new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER), message)
               .create();
    }
}
