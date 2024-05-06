package com.michaelvol.bankingapp.account;

import com.michaelvol.bankingapp.account.dto.CreateAccountRequestDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.repository.AccountRepository;
import com.michaelvol.bankingapp.account.samples.AccountSamples;
import com.michaelvol.bankingapp.account.service.impl.AccountServiceImpl;
import com.michaelvol.bankingapp.employee.entity.Employee;
import com.michaelvol.bankingapp.employee.service.EmployeeService;
import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.holder.service.HolderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
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

    @Test
    public void createAccount_success() {
        Holder sampleHolder = Holder.builder().id(1L).build();
        Employee sampleEmployee = Employee.builder().id(1L).build();
        Account sampleAccount = AccountSamples.sampleAccount(1L, sampleHolder,
                                                             sampleEmployee);

        when(holderService.getHolderById(1L)).thenReturn(sampleHolder);
        when(employeeService.getEmployeeById(1L)).thenReturn(sampleEmployee);
        when(accountRepository.save(any(Account.class))).thenReturn(sampleAccount);

        CreateAccountRequestDto input = CreateAccountRequestDto.builder()
                                                               .accountType(sampleAccount.getAccountType())
                                                               .managerId(1L)
                                                               .holderId(1L)
                                                               .currencyCode("EUR").build();

        Account returnedAccount = accountService.createAccount(input);
        verify(holderService).getHolderById(1L);
        verify(employeeService).getEmployeeById(1L);
        verify(accountRepository).save(any(Account.class));
        assertEquals(1L, returnedAccount.getManager().getId());
        assertEquals(1L, returnedAccount.getHolder().getId());
        assertEquals(sampleAccount.getCurrency(), returnedAccount.getCurrency());
        assertEquals(returnedAccount.getBalance(), BigDecimal.ZERO);
        assertEquals(returnedAccount.getAccountType(), sampleAccount.getAccountType());
    }
}
