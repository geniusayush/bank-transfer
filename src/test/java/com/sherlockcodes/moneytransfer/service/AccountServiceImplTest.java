package com.sherlockcodes.moneytransfer.service;

import com.sherlockcodes.moneytransfer.controller.AccountInput;
import com.sherlockcodes.moneytransfer.controller.TransactionInput;
import com.sherlockcodes.moneytransfer.model.Account;
import com.sherlockcodes.moneytransfer.repository.AccountRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountServiceImplTest {

    private AccountRepositoryImpl accountRepo;
    private AccountServiceImpl service;

    @BeforeEach
    void setup() {
        this.accountRepo = Mockito.mock(AccountRepositoryImpl.class);
        this.service = new AccountServiceImpl(accountRepo);
    }

    @Test
    void addAccountAlreadyExistTest() {
        Mockito.when(accountRepo.findAccount("a", 123)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.addAccount(new AccountInput("a", 123, 20)));
    }

    @Test
    void addAccountTest() {
        Mockito.when(accountRepo.findAccount("a", 123)).thenReturn(false);
        ArgumentCaptor<Account> argument = ArgumentCaptor.forClass(Account.class);
        service.addAccount(new AccountInput("a", 123, 20));
        Mockito.verify(accountRepo).saveAccount(argument.capture());
        assertEquals("a", argument.getValue().getName());

    }


    @Test
    void createTransactionNullAccountTest() {
        Mockito.when(accountRepo.getAccount(123)).thenReturn(null);
        Mockito.when(accountRepo.getAccount(234)).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> service.createTransaction(123, new TransactionInput(234, 34)));
    }

    @Test
    void createTransactionInvalidAmountTest() {
        Account a = Account.builder().balance(50).name("a").phone(123).build();
        Account b = Account.builder().balance(50).name("b").phone(234).build();

        Mockito.when(accountRepo.getAccount(123)).thenReturn(a);
        Mockito.when(accountRepo.getAccount(234)).thenReturn(b);
        assertThrows(IllegalArgumentException.class, () -> service.createTransaction(123, new TransactionInput(234, 100)));
    }
}