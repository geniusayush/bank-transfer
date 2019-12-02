package com.sherlockcodes.moneytransfer.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Builder
@Getter
@Setter
public class Account {
    private final ReadWriteLock accountLock = new ReentrantReadWriteLock();
    double balance;
    private String name;
    private long phone;
    private String desc;
    private long id;

    public double getBalance() {
        this.accountLock.readLock().lock();
        try {
            return balance;
        } finally {
            this.accountLock.readLock().unlock();
        }
    }
}
