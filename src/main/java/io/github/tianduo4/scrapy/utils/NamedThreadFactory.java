package io.github.tianduo4.scrapy.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.LongAdder;

/**
 *
 *
 * @author xushipeng
 * @date 2018/3/12
 */
public class NamedThreadFactory implements ThreadFactory {

    private final String prefix;
    private final LongAdder threadNumber = new LongAdder();

    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        threadNumber.add(1);
        return new Thread(runnable, prefix + "@thread-" + threadNumber.intValue());
    }
}