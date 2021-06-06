package com.kddimitrov.exchangeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * The purpose of this class is to make sure that all exchange clients will start at once when the application is ready
 * <p></p>
 *
 * @see ApplicationReadyEvent
 * @see com.kddimitrov.exchangeClient.ExchangeClient
 */
@Component
public class OnStartupTaskExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnStartupTaskExecutor.class);

    private final TaskExecutor taskExecutor;
    private final ApplicationContext applicationContext;

    public OnStartupTaskExecutor(TaskExecutor taskExecutor,
                                 ApplicationContext applicationContext) {
        this.taskExecutor = taskExecutor;
        this.applicationContext = applicationContext;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void atStartup() {
        CountDownLatch latch = new CountDownLatch(1);

        applicationContext
                .getBeansWithAnnotation(ExchangeClient.class)
                .values()
                .forEach(bean -> {
                    taskExecutor.execute((Runnable) bean);
                    LOGGER.info("###### Starting exchange client : {} ######", bean.getClass());
                });

        latch.countDown();
    }
}