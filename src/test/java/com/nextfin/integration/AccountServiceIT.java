package com.nextfin.integration;

import com.nextfin.account.dto.CreateAccountRequestDto;
import com.nextfin.account.entity.Account;
import com.nextfin.account.enums.AccountType;
import com.nextfin.account.repository.AccountRepository;
import com.nextfin.account.service.core.AccountService;
import com.nextfin.employee.entity.Employee;
import com.nextfin.employee.repository.EmployeeRepository;
import com.nextfin.exceptions.exception.NotFoundException;
import com.nextfin.holder.repository.HolderRepository;
import com.nextfin.integration.utils.WithMockNextfinUser;
import com.nextfin.users.entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        jdbcTemplate.execute("ALTER SEQUENCE accounts_seq RESTART WITH 1");
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
    @WithMockNextfinUser
    public void shouldCreateAccount() {
        LocalDate localDate = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();
        Employee manager = Employee.builder().firstName("Jane").lastName("Smith").dateOfBirth(localDate).phoneNumber("0987654321")
                                   .build();
        manager = employeeRepository.save(manager);
        CreateAccountRequestDto dto = CreateAccountRequestDto.builder().managerId(manager.getId()).build();
        holderRepository.save(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getHolder());
        Account account = sut.createAccount(dto);
        assertNotNull(account);
        assertEquals(AccountType.SAVINGS, account.getAccountType());
        assertEquals(manager.getId(), account.getManager().getId());
        assertEquals(Currency.getInstance("EUR"), account.getCurrency());
    }
}
