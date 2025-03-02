package com.michaelvol.nextfin.account;

import com.michaelvol.nextfin.account.dto.CreateAccountRequestDto;
import com.michaelvol.nextfin.account.entity.Account;
import com.michaelvol.nextfin.account.enums.AccountType;
import com.michaelvol.nextfin.account.repository.AccountRepository;
import com.michaelvol.nextfin.account.service.core.AccountService;
import com.michaelvol.nextfin.employee.entity.Employee;
import com.michaelvol.nextfin.employee.repository.EmployeeRepository;
import com.michaelvol.nextfin.exceptions.exception.NotFoundException;
import com.michaelvol.nextfin.holder.entity.Holder;
import com.michaelvol.nextfin.holder.repository.HolderRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
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

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    public void setup() {
        Account account =
                Account.builder()
                       .accountType(AccountType.SAVINGS)
                       .currency(Currency.getInstance("EUR"))
                       .build();
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
    @WithUserDetails(userDetailsServiceBeanName = "userServiceImpl")
    public void shouldCreateAccount() {
        LocalDate localDate = Instant.now()
                                     .atZone(ZoneId.systemDefault())
                                     .toLocalDate();
        Holder holder = Holder.builder()
                              .firstName("John")
                              .lastName("Doe")
                              .dateOfBirth(localDate)
                              .phoneNumber("1234567890")
                              .build();
        Employee manager = Employee.builder()
                                   .firstName("Jane")
                                   .lastName("Smith")
                                   .dateOfBirth(localDate)
                                   .phoneNumber("0987654321")
                                   .build();
        holder = holderRepository.save(holder);
        manager = employeeRepository.save(manager);
        CreateAccountRequestDto dto =
                CreateAccountRequestDto.builder()
                                       .holderId(holder.getId())
                                       .managerId(manager.getId())
                                       .build();
        Account account = sut.createAccount(dto);
        assertNotNull(account);
        assertEquals(AccountType.SAVINGS, account.getAccountType());
        assertEquals(holder.getId(), account.getHolder().getId());
        assertEquals(manager.getId(), account.getManager().getId());
        assertEquals(Currency.getInstance("EUR"), account.getCurrency());
    }
}
