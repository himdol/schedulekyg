package com.schedule.schedulekyg.utils.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
	
	// 엑셀 헤더명
	String headerName() default "";
	
	// 엑셀다운로드 시 DB 결과 컬럼명 정의
	String columnName() default "";
	
	// 컬럼 순서
	int col() default 0;
}
