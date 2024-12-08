package com.michaelvol.bankingapp.organization.bill.dto;

import java.util.UUID;

public record CreateBillDto(
        String name,
        double amount,
        String dueDate,
        String status,
        UUID organizationId
) {
}
