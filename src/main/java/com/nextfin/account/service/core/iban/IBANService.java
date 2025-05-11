package com.nextfin.account.service.core.iban;

import com.nextfin.account.entity.Account;
import com.nextfin.exceptions.exception.MissingAccountIdException;
import lombok.RequiredArgsConstructor;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IBANService {


    @Value("${com.nextfin.countryCode}")
    private String countryCode;

    @Value("${com.nextfin.bankCode}")
    private String bankCode;

    public Iban generateIBAN(Account account) {
        if (account.getId() == null) throw new MissingAccountIdException();

        return new Iban.Builder().countryCode(CountryCode.getByCode(countryCode)).bankCode(bankCode).accountNumber(
                account.getId().toString()).build();
    }
}
