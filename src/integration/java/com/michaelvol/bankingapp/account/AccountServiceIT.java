package com.michaelvol.bankingapp.account;

import com.michaelvol.bankingapp.account.dto.CreateAccountRequestDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.enums.AccountType;
import com.michaelvol.bankingapp.account.repository.AccountRepository;
import com.michaelvol.bankingapp.account.service.core.AccountService;
import com.michaelvol.bankingapp.employee.entity.Employee;
import com.michaelvol.bankingapp.employee.repository.EmployeeRepository;
import com.michaelvol.bankingapp.exceptions.exception.NotFoundException;
import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.holder.repository.HolderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("it")
public class AccountServiceIT {

    @Autowired
    private AccountService sut;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private HolderRepository holderRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup() {
        Account account = Account.builder().accountType(AccountType.SAVINGS).currency(Currency.getInstance("EUR")).build();
        accountRepository.save(account);
    }

    @AfterEach
    public void tearDown() {
        accountRepository.deleteById(1L);
    }

    @Test
    public void shouldGetAccountByIdWhenAccountExists() {
        Account account = sut.getAccount(1L);
        assertNotNull(account);
        assertEquals(AccountType.SAVINGS, account.getAccountType());
        assertEquals(1L, account.getId());
        assertEquals(Currency.getInstance("EUR"), account.getCurrency());
    }

    @Test
    public void shouldThrowExceptionWhenAccountDoesNotExist() {
        assertThrows(NotFoundException.class, () -> sut.getAccount(1234L));
    }

    @Test
    public void shouldCreateAccount() {
        Holder holder = Holder.builder().firstName("John").lastName("Doe").build();
        Employee manager = Employee.builder().firstName("Jane").lastName("Smith").build();
        holder = holderRepository.save(holder);
        manager = employeeRepository.save(manager);
        CreateAccountRequestDto dto = CreateAccountRequestDto.builder().holderId(holder.getId()).managerId(manager.getId()).build();
        Account account = sut.createAccount(dto);
        assertNotNull(account);
        assertEquals(AccountType.SAVINGS, account.getAccountType());
        assertEquals(holder.getId(), account.getHolder().getId());
        assertEquals(manager.getId(), account.getManager().getId());
        assertEquals(Currency.getInstance("EUR"), account.getCurrency());
    }
}
