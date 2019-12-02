package com.sherlockcodes.moneytransfer.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountInput {
    private String name;
    private long phone;
    private long  initBalance;
}
