package com.chendu.jq.webServer.controller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.*;

public abstract class Controller {
    @Value("${rest.request.timeout}")
    protected Integer timeoutSeconds;

    protected ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("controller-pool-%d").build();

    /**
     * 线程池
     */
    protected ExecutorService executorService = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
}
