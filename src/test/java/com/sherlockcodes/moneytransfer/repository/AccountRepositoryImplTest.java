package com.sherlockcodes.moneytransfer.repository;

import com.sherlockcodes.moneytransfer.model.Account;
import com.sherlockcodes.moneytransfer.repository.generator.IdGenerator;
import com.sherlockcodes.moneytransfer.repository.generator.IdGeneratorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountRepositoryImplTest {

    private AccountRepository repo;
    private IdGenerator idGenerator;

    @BeforeEach
    void setup() {
        idGenerator = new IdGeneratorImpl();
        repo = new AccountRepositoryImpl(idGenerator);
    }

    @Test
    void HandleTransactionTest() {
        Account a = repo.saveAccount(Account.builder().balance(50).name("a").phone(123).build());
        Account b = repo.saveAccount(Account.builder().balance(50).name("b").phone(234).build());

        repo.handleTransaction(a, b, 15);

        assertEquals(35, a.getBalance());
        assertEquals(65, b.getBalance());

    }    @Test
    void HandleTransactionAsyncTest() throws InterruptedException {
        Account a = repo.saveAccount(Account.builder().balance(50).name("a").phone(123).build());
        Account b = repo.saveAccount(Account.builder().balance(50).name("b").phone(234).build());
        Thread thread = new Thread(()->repo.handleTransaction(a, b, 15));
        thread.start();
        thread.join();

        assertEquals(35, a.getBalance());
        assertEquals(65, b.getBalance());

    }    @Test
    void HandleTransactionAsync3Test() throws ExecutionException, InterruptedException {
        Account a = repo.saveAccount(Account.builder().balance(50).name("a").phone(123).build());
        Account b = repo.saveAccount(Account.builder().balance(50).name("b").phone(234).build());
        Account c = repo.saveAccount(Account.builder().balance(50).name("c").phone(345).build());
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<?> future1 = pool.submit(() -> repo.handleTransaction(a, b, 15));
        Future<?> future2 = pool.submit(() -> repo.handleTransaction(b, c, 30));
        future1.get();
        future2.get();
        assertEquals(35, a.getBalance());
        assertEquals(35, b.getBalance());
        assertEquals(80, c.getBalance());

    }  @Test
    void HandleTransactionDeadlockTest() throws ExecutionException, InterruptedException {
        Account a = repo.saveAccount(Account.builder().balance(50).name("a").phone(123).build());
        Account b = repo.saveAccount(Account.builder().balance(50).name("b").phone(234).build());
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<?> future1 = pool.submit(() -> repo.handleTransaction(a, b, 15));
        Future<?> future2 = pool.submit(() -> repo.handleTransaction(b, a, 30));

        future1.get();
        future2.get();
        assertEquals(65, a.getBalance());
        assertEquals(35, b.getBalance());
    }
}