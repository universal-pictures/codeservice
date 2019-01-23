package com.universalinvents.codeservice.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

public class CodeServiceRetryListener implements RetryListener {

    private final Logger log = LoggerFactory.getLogger(CodeServiceRetryListener.class);

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        // NOOP
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        // Log the retry
        log.warn("Retry attempt " + context.getRetryCount() + " after error occurred: " + throwable.getMessage());
    }
}
