package com.sherlockcodes.moneytransfer.controller;

import com.sherlockcodes.moneytransfer.model.Account;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AccountOutput {
    private final double balance;
    private final long id;
    private final String name;
    private final long phone;

    public AccountOutput(Account account) {
        this.id = account.getId();
        this.balance = account.getBalance();
        this.name = account.getName();
        this.phone = account.getPhone();
    }
}
