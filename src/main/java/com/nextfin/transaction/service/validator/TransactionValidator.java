package com.nextfin.transaction.service.validator;

import com.nextfin.transaction.dto.TransferRequestDto;

public interface TransactionValidator {
    /**
     * Makes all the necessary checks & validation to ensure that a transaction can be processed. Any potential
     * validation errors with throw the relevant exception. If the transaction has been validated with no errors
     * null is returned.
     * @param dto - the dto containing the transaction details
     */
    void validate(TransferRequestDto dto);
}
