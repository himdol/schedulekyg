package com.schedule.schedulekyg.utils.excel.exception;

public interface ExceptionSupplier<T> {
    T get() throws Exception;
}
