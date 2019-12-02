package com.sherlockcodes.moneytransfer.service;

import com.sherlockcodes.moneytransfer.controller.AccountInput;
import com.sherlockcodes.moneytransfer.controller.TransactionInput;
import com.sherlockcodes.moneytransfer.model.Account;
import com.sherlockcodes.moneytransfer.repository.AccountRepository;

import javax.inject.Inject;
import java.util.NoSuchElementException;

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Inject
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account addAccount(AccountInput input) {
        boolean accountExist = accountRepository.findAccount(input.getName(), input.getPhone());
        if (accountExist) {
            throw new IllegalArgumentException("Account with same name and phone already exists");
        }
        Account account = Account.builder().balance(input.getInitBalance())
                .phone(input.getPhone())
                .name(input.getName()).build();
        account = accountRepository.saveAccount(account);
        return account;
    }

    @Override
    public Account getAccount(long input) {
        return accountRepository.getAccount(input);
    }

    @Override
    public void createTransaction(long sourceAccountId, TransactionInput input) {
        Account sourceAccount = accountRepository.getAccount(sourceAccountId);
        Account destAccount = accountRepository.getAccount(input.getDestinationAccountId());
        if (sourceAccount == null || destAccount == null)
            throw new NoSuchElementException("source and destination accounts should be valid");
        if (sourceAccount.getBalance() < input.getAmount()) throw new IllegalArgumentException("insufficient Balance");
        accountRepository.handleTransaction(sourceAccount, destAccount, input.getAmount());
    }
}
