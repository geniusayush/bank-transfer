package com.sherlockcodes.moneytransfer.repository;

import com.sherlockcodes.moneytransfer.model.Account;

public interface AccountRepository {
    boolean findAccount(String name, long phone);

    Account saveAccount(Account account);

    //null if not exist
    Account getAccount(long input);

    void handleTransaction(Account sourceAccount, Account destinationAccountId, double amount);
}
