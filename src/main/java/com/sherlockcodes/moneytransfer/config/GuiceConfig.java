package com.sherlockcodes.moneytransfer.config;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.sherlockcodes.moneytransfer.controller.AppController;
import com.sherlockcodes.moneytransfer.controller.AppControllerImpl;
import com.sherlockcodes.moneytransfer.repository.AccountRepository;
import com.sherlockcodes.moneytransfer.repository.AccountRepositoryImpl;
import com.sherlockcodes.moneytransfer.repository.generator.IdGenerator;
import com.sherlockcodes.moneytransfer.repository.generator.IdGeneratorImpl;
import com.sherlockcodes.moneytransfer.service.AccountService;
import com.sherlockcodes.moneytransfer.service.AccountServiceImpl;


public class GuiceConfig extends AbstractModule {


    @Override
    protected void configure() {
        bind(AppController.class).to(AppControllerImpl.class).in(Scopes.SINGLETON);
        bind(AccountService.class).to(AccountServiceImpl.class).in(Scopes.SINGLETON);
        bind(AccountRepository.class).to(AccountRepositoryImpl.class).in(Scopes.SINGLETON);
        bind(IdGenerator.class).to(IdGeneratorImpl.class).in(Scopes.SINGLETON);

    }

}
