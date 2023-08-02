package com.schedule.schedulekyg.utils.excel.exception;

/**
 * 엑셀 업로드 Field 에러
 *
 */
public class ExcelReaderFieldException extends RuntimeException {

    private static final long serialVersionUID = 958378807543925168L;

    public ExcelReaderFieldException() {
    }

    public ExcelReaderFieldException(String message) {
        super(message);
    }

    public ExcelReaderFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelReaderFieldException(Throwable cause) {
        super(cause);
    }

    public ExcelReaderFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
