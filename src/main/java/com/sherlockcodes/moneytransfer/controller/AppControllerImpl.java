package com.sherlockcodes.moneytransfer.controller;

import com.google.gson.JsonSyntaxException;
import com.sherlockcodes.moneytransfer.model.Account;
import com.sherlockcodes.moneytransfer.service.AccountService;
import com.sherlockcodes.moneytransfer.utils.JsonUtils;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.NoSuchElementException;

public class AppControllerImpl implements AppController {
    private final AccountService accountService;

    @Inject
    public AppControllerImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public String createAccount(Request request, Response response) {
        AccountInput input;
        try {
            input = JsonUtils.make().fromJson(request.body(), AccountInput.class);
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
        if (input.getName().length() < 3) throw new IllegalArgumentException("At least 3 letters needed in the name");
        if(input.getInitBalance()<0) throw new IllegalArgumentException("the initial balance should be positive");
        Account account = accountService.addAccount(input);
        response.status(201);
        return JsonUtils.make().toJson(new AccountOutput(account));
    }


    @Override
    public String getAccountById(Request request, Response response) {
        String inputString = request.params("id");
        long input;
        try {
            input = Long.valueOf(inputString);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(ex);
        }
        if (input < 0) throw new IllegalArgumentException("Account id cannot be negative");
        Account account = accountService.getAccount(input);
        if (account == null) {
            throw new NoSuchElementException("Account does not exist");
        }
        response.status(200);
        return JsonUtils.make().toJson(new AccountOutput(account));
    }


    @Override
    public String createAccountTransaction(Request request, Response response) {
        String accountString = request.params("id");
        long sourceAccount;
        try {
            sourceAccount = Long.valueOf(accountString);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(ex);
        }
        TransactionInput input;
        try {
            input = JsonUtils.make().fromJson(request.body(), TransactionInput.class);
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
        if (input.getDestinationAccountId() < 0 || input.getAmount() < 0 || sourceAccount < 0)
            throw new IllegalArgumentException("Inputs cannot be negative");
        accountService.createTransaction(sourceAccount, input);
        response.status(204);
        return "";
    }
}
