package com.schedule.schedulekyg.utils.excel.exception;

/**
 * 엑셀 업로드 읽을 수 없는 파일 에러
 *
 */
public class ExcelReaderFileException extends RuntimeException {

    private static final long serialVersionUID = 2456540561094569808L;

    public ExcelReaderFileException() {
    }

    public ExcelReaderFileException(String message) {
        super(message);
    }

    public ExcelReaderFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelReaderFileException(Throwable cause) {
        super(cause);
    }

    public ExcelReaderFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
