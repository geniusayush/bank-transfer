package com.sherlockcodes.moneytransfer.service;

import com.sherlockcodes.moneytransfer.controller.AccountInput;
import com.sherlockcodes.moneytransfer.model.Account;
import com.sherlockcodes.moneytransfer.controller.TransactionInput;

public interface AccountService {
    Account addAccount(AccountInput input);

    Account getAccount(long input);

    void createTransaction(long sourceAccountId, TransactionInput input);
}
