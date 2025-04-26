package com.nextfin.organization.enums;

import com.nextfin.transaction.entity.TransactionCategory;
import lombok.Getter;

import java.util.Arrays;

/**
 * Contains the transaction categories relevant for organizations.
 */
@Getter
public enum IndustryType {
    TRANSPORT(TransactionCategory.TRANSPORT), RESTAURANTS(TransactionCategory.RESTAURANTS), SHOPPING(
            TransactionCategory.SHOPPING), BILLS(TransactionCategory.BILLS), ENTERTAINMENT(
            TransactionCategory.ENTERTAINMENT), HEALTH(TransactionCategory.HEALTH), EDUCATION(TransactionCategory.EDUCATION);

    private final TransactionCategory category;

    IndustryType(TransactionCategory category) {
        this.category = category;
    }

    // Reverse lookup by TransactionCategory
    public static IndustryType fromTransactionCategory(TransactionCategory category) {
        return Arrays.stream(values()).filter(value -> value.getCategory().equals(category)).findAny().orElseThrow(
                () -> new IllegalArgumentException("No IndustryType found for category: " + category));
    }
}
