package com.sherlockcodes.moneytransfer.repository;

import com.sherlockcodes.moneytransfer.model.Account;
import com.sherlockcodes.moneytransfer.repository.generator.IdGenerator;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class AccountRepositoryImpl implements AccountRepository {
    private final Map<Long, Account> accountMap = new ConcurrentHashMap<>();
    private final IdGenerator idGenerator;

    @Inject
    public AccountRepositoryImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }


    @Override
    public boolean findAccount(String name, long phone) {
        return accountMap.values().stream().anyMatch(k -> k.getName().equals(name) && k.getPhone() == phone);
    }

    @Override
    public Account saveAccount(Account account) {
        long id = idGenerator.generateNext();
        account.setId(id);
        accountMap.put(id, account);
        return account;
    }

    //null if not exist
    @Override
    public Account getAccount(long input) {
        if (!accountMap.containsKey(input)) return null;
        return accountMap.get(input);
    }

    @Override
    public void handleTransaction(Account sourceAccount, Account destinationAccount, double amount) {

        final Account first = sourceAccount.getId() < destinationAccount.getId() ? sourceAccount : destinationAccount;
        final Account second = first == sourceAccount ? destinationAccount : sourceAccount;
        final Lock lockF = first.getAccountLock().writeLock();
        final Lock lockS = second.getAccountLock().writeLock();
        boolean done = false;
        do {
            try {
                try {
                    if (lockF.tryLock(getWait(), MILLISECONDS)) {
                        try {
                            if (lockS.tryLock(getWait(), MILLISECONDS)) {

                                sourceAccount.setBalance(sourceAccount.getBalance() - amount);
                                destinationAccount.setBalance(destinationAccount.getBalance() + amount);
                                done = true;
                            }
                        } finally {
                            lockS.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException("Cancelled");
                }
            } finally {
                lockF.unlock();
            }
        } while (!done);

    }

    private long getWait() {
        return (long) (Math.random() * 1000);
    }
}
