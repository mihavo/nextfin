package com.nextfin.service.account;

import com.nextfin.account.dto.CreateAccountRequestDto;
import com.nextfin.account.dto.DepositAmountRequestDto;
import com.nextfin.account.dto.GetAccountBalanceDto;
import com.nextfin.account.dto.WithdrawAmountRequestDto;
import com.nextfin.account.entity.Account;
import com.nextfin.account.repository.AccountRepository;
import com.nextfin.account.service.core.impl.AccountServiceImpl;
import com.nextfin.employee.entity.Employee;
import com.nextfin.employee.service.EmployeeService;
import com.nextfin.exceptions.exception.NotFoundException;
import com.nextfin.holder.entity.Holder;
import com.nextfin.holder.service.HolderService;
import com.nextfin.service.samples.AccountSamples;
import com.nextfin.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestAccountService {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private HolderService holderService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private SecurityContext securityContext;

    Account sampleAccount;

    @BeforeEach
    public void init() {
        sampleAccount = AccountSamples.sampleAccount(1L, new Holder(), new Employee());
    }

    //    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(accountRepository, holderService, employeeService, messageSource, securityContext);
    }

    @Test
    public void createAccount() {
        UUID sampleUUID = UUID.randomUUID();
        Holder sampleHolder = Holder.builder().id(sampleUUID).build();
        User user = User.builder().holder(sampleHolder).build();
        Employee sampleEmployee = Employee.builder().id(1L).build();
        Account sampleAccount = AccountSamples.sampleAccount(1L, sampleHolder,
                                                             sampleEmployee);

        when(holderService.getHolderById(sampleUUID)).thenReturn(sampleHolder);
        when(employeeService.getEmployeeById(1L)).thenReturn(sampleEmployee);
        when(accountRepository.save(any(Account.class))).thenReturn(sampleAccount);
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken(user,
                null,
                null));
        SecurityContextHolder.setContext(securityContext);
        CreateAccountRequestDto input = CreateAccountRequestDto.builder()
                                                               .accountType(sampleAccount.getAccountType())
                                                               .managerId(1L)
                                                               .holderId(sampleUUID)
                                                               .currencyCode("EUR").build();

        Account returnedAccount = accountService.createAccount(input);
        verify(holderService).getHolderById(sampleUUID);
        verify(employeeService).getEmployeeById(1L);
        verify(accountRepository).save(any(Account.class));
        assertEquals(1L, returnedAccount.getManager().getId());
        assertEquals(sampleUUID, returnedAccount.getHolder().getId());
        assertEquals(sampleAccount.getCurrency(), returnedAccount.getCurrency());
        assertEquals(BigDecimal.ZERO, returnedAccount.getBalance());
        assertEquals(returnedAccount.getAccountType(), sampleAccount.getAccountType());
    }

    @Test
    public void getAccount_success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sampleAccount));
        Account account = accountService.getAccount(1L);
        verify(accountRepository).findById(1L);
        assertEquals(sampleAccount, account);
    }

    @Test
    public void getAccount_notFound() {
        assertThrows(NotFoundException.class, () -> accountService.getAccount(1L));
    }

    @Test
    public void checkBalance() {
        sampleAccount.setBalance(new BigDecimal("50.00"));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sampleAccount));
        GetAccountBalanceDto result = accountService.checkBalance(1L);
        assertEquals(sampleAccount.getCurrency(), result.currency());
        assertEquals(new BigDecimal("50.00"), result.balance());
    }

    @Test
    public void depositAmount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sampleAccount));
        BigDecimal amount = new BigDecimal("50.00");
        DepositAmountRequestDto dto = new DepositAmountRequestDto(amount);
        accountService.depositAmount(1L, dto);
        accountService.depositAmount(1L, dto);
        BigDecimal result = accountService.depositAmount(1L, dto);
        assertEquals(new BigDecimal("150.00"), result);
    }

    @Test
    public void withdrawAmount_success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sampleAccount));
        sampleAccount.setBalance(new BigDecimal("75.00"));
        BigDecimal amount = new BigDecimal("50.00");
        WithdrawAmountRequestDto dto = new WithdrawAmountRequestDto(amount);
        BigDecimal balance = accountService.withdrawAmount(1L, dto);
        assertEquals(new BigDecimal("25.00"), balance);
    }

    @Test
    public void withdrawAmount_insufficientFunds() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sampleAccount));
        BigDecimal amount = new BigDecimal("50.00");
        WithdrawAmountRequestDto dto = new WithdrawAmountRequestDto(amount);
        assertThrows(ResponseStatusException.class,
                     () -> accountService.withdrawAmount(1L, dto)).getMessage();
    }
}
