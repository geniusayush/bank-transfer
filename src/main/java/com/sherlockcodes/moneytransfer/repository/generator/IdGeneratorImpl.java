package com.sherlockcodes.moneytransfer.repository.generator;

import com.google.common.collect.AbstractSequentialIterator;

public class IdGeneratorImpl implements IdGenerator {
    private final AbstractSequentialIterator<Long> generator;

    public IdGeneratorImpl() {
        generator = new AbstractSequentialIterator<Long>(1L) {
            @Override
            protected Long computeNext(Long previous) {
                return Long.MAX_VALUE == previous ? null : previous + 1;
            }
        };
    }

    @Override
    public long generateNext() {
        return generator.next();
    }
}
