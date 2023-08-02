package com.schedule.schedulekyg.utils.excel;

import com.github.drapostolos.typeparser.TypeParser;
import com.schedule.schedulekyg.utils.excel.annotation.ExcelColumn;
import com.schedule.schedulekyg.utils.excel.exception.ExcelReaderFieldException;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class ExcelUtils {
	
	public static Validator validator;
	
	public static Validator getValidator() {
		if(validator == null) {
	        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	        validator = factory.getValidator();
		}
		return validator;
	}
	
    /**
     * Cell 데이터를 Type 별로 체크 하여 String 데이터로 변환함
     * String 데이터로 우선 변환해야 함
     * @param cell 요청 엑셀 파일의 cell 데이터
     * @return String 형으로 변환된 cell 데이터
     */
    public static String getValue(Cell cell) {

        String value = null;
        // 셀 내용의 유형 판별
        switch (cell.getCellType()) {
            case STRING: // getRichStringCellValue() 메소드를 사용하여 컨텐츠를 읽음
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC: // 날짜 또는 숫자를 포함 할 수 있으며 아래와 같이 읽음
                if (DateUtil.isCellDateFormatted(cell))
                	value = cell.getLocalDateTimeCellValue().toLocalDate().toString();
                else
                    value = String.valueOf(cell.getNumericCellValue());
                    if (value.endsWith(".0"))
                        value = value.substring(0, value.length() - 2);
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                value = String.valueOf(cell.getCellFormula());
                break;
            case ERROR:
                value = ErrorEval.getText(cell.getErrorCellValue());
                break;
            case BLANK:
            case _NONE:
            default:
                value = "";
        }

        return value;
    }

    /**
     * TypeParser 로 String으로 변환된 Cell 데이터를 객체 필드 타입에 맞게 변환하여 셋팅해줌
     * @param object 요청 객체
     * @param <T>
     * @param row 엑셀 ROW 데이터
     * @return Cell 데이터를 맵핑한 오브젝트
     */
    public static <T> T setObjectMapping(T object, Row row) throws Exception {

        int i = 0;

        if(Objects.isNull(object)) return null;

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String cellValue = null;
            TypeParser typeParser = TypeParser.newBuilder().build();
            Iterator<Cell> cells = row.cellIterator();
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);

            while(cells.hasNext()) {
                Cell cell = cells.next();
                int index = cell.getColumnIndex();
                if(excelColumn.col() == index) {
                    cellValue = getValue(cell);
                    Object setData = typeParser.parseType(cellValue, field.getType());
                    field.set(object, setData);
                }
            }
        }
        rowValidation(object);
        return object;
    }

    /**
     * 객체에 대한 Validation 을 검증해서 검증을 통과 하지 못한 내역이 있을 경우 에러 리스트에 담는다
     * @param object
     * @param row
     * @param i
     * @param <T>
     */
    private static <T> void checkValidation(T object, Row row, int i, String cellValue, Field field) {
    	Validator validator = getValidator();
        Set<ConstraintViolation<T>> constraintValidations = validator.validate(object);
        String fieldName = field.getName();

        ConstraintViolation<T> validData = constraintValidations.stream()
                .filter(data -> data.getPropertyPath().toString().equals(fieldName))
                .findFirst().orElse(null);

        if(Objects.isNull(validData)) return;

        if(validData.getMessageTemplate().contains("NotEmpty") || validData.getMessageTemplate().contains("NotNull")) {
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            String headerName = excelColumn.headerName();
            throw new ExcelReaderFieldException(headerName.concat("값은 필수입니다."));
        }
    }

    /**
     * Object Validation 추가
     * @param object
     */
    private static <T> void rowValidation(T object) throws NoSuchFieldException {
        Validator validator = getValidator();
        Set<ConstraintViolation<T>> constraintValidations = validator.validate(object);
        ConstraintViolation<T> validData = constraintValidations.stream().findFirst().orElse(null);

        if(validData == null) {
            return;
        }

        if(isNotNullValidation(validData)) {
            String fieldName = validData.getPropertyPath().toString();
            Field field = object.getClass().getDeclaredField(fieldName);
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            String headerName = excelColumn.headerName();
            throw new ExcelReaderFieldException(headerName.concat("값은 필수입니다."));
        }

        throw new ExcelReaderFieldException(validData.getMessage());
    }

    private static <T> boolean isNotNullValidation(ConstraintViolation<T> validData) {
        if(validData.getConstraintDescriptor().getAnnotation() instanceof NotNull) {
            return true;
        }
        if(validData.getConstraintDescriptor().getAnnotation() instanceof NotEmpty) {
            return true;
        }
        return false;
    }
}
