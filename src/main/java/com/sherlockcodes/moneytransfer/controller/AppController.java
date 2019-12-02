package com.sherlockcodes.moneytransfer.controller;

import spark.Request;
import spark.Response;

public interface AppController {
    Object createAccount(Request request, Response response);

    Object getAccountById(Request request, Response response);

    Object createAccountTransaction(Request request, Response response);
}
