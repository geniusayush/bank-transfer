package com.sherlockcodes.moneytransfer.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionInput {
private long destinationAccountId;
private double amount;
}
