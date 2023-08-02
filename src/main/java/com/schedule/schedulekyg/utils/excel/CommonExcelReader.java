package com.schedule.schedulekyg.utils.excel;

import com.schedule.schedulekyg.utils.excel.annotation.ExcelColumn;
import com.schedule.schedulekyg.utils.excel.exception.ExcelReaderFieldException;
import com.schedule.schedulekyg.utils.excel.exception.ExcelReaderFileException;
import com.schedule.schedulekyg.utils.excel.exception.ExceptionSupplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class CommonExcelReader<T> {
	
	private Class<T> excelDto;
	
	public CommonExcelReader(Class<T> excelDto) {
		this.excelDto = excelDto;
	}
	
    /**
     * excelDto 어노테이션(@ExcelColumn) 정보(headerName, col) 로 엑셀 업로드 파일의 헤더가 유효한지 검증한다.
     * @param row
     * @return
     */
	private void excelHeaderValidation(Row row) {

		// dto 객체에서 필드 정보를 가져온다.
		Field[] fields = excelDto.getDeclaredFields();
		
		if(ObjectUtils.isEmpty(fields)) {
			throw new ExcelReaderFieldException("엑셀 업로드 dto오류. 관리자에게 문의해주세요.");
		}
		
		for(Field field : fields) {
			// 선언된 필드의 어노테이션을 가져온다.
			ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
			if(excelColumn == null) {
				throw new ExcelReaderFieldException("엑셀 업로드 설정 오류입니다(annotation). 관리자에게 문의해주세요.");
			}
			String headerName = excelColumn.headerName();
			int col = excelColumn.col();
			
			// dto에 headerName 값이 정의되어 있지 않으면,
			if(StringUtils.isEmpty(headerName)) {
				throw new ExcelReaderFieldException("엑셀 업로드 설정 오류입니다(headerName). 관리자에게 문의해주세요.");
			}
			
			// 업로드 파일의 헤더명을 가져온다.
			String fileHeaderName = ExcelUtils.getValue(row.getCell(col));
			if(StringUtils.isEmpty(fileHeaderName)) {
				throw new ExcelReaderFieldException("엑셀 업로드 파일의 양식을 확인해주세요. (헤더오류)");
			}

			// dto 헤더명과 업로드파일의 헤더명이 같은지 체크한다.
			if(!StringUtils.equals(headerName, fileHeaderName)) {
				throw new ExcelReaderFieldException("엑셀 업로드 파일 헤더매칭 오류입니다.");
			}
		}

	}
	
	public List<T> getObjectList(final MultipartFile multipartFile) throws Exception {
        
		// 엑셀 파일을 Workbook에 담는다
        final Workbook workbook;
        
        // 엑셀파일 매핑 결과 리스트
        List<T> objectList;

        try {
			workbook = WorkbookFactory.create(multipartFile.getInputStream());

            int startRow = 1;
	        // 시트 수 (첫번째에만 존재시 0)
	        final Sheet sheet = workbook.getSheetAt(0);
	        
	        // 전체 행 수
	        final int rowCount = sheet.getPhysicalNumberOfRows();

	        // 헤더를 가져온다.
	        Row headerRow = sheet.getRow(0);
	        
	        // 엑셀 업로드 양식을 체크한다.
	        excelHeaderValidation(headerRow);

	        // row 데이터를 dto로 매핑한다.
	        objectList = IntStream
	                .range(startRow, rowCount)
//	                .filter(rowIndex -> isPass(sheet.getRow(rowIndex)))
	                .mapToObj(rowIndex -> wrap(() -> setObjectMapping(sheet.getRow(rowIndex))))
	                .collect(Collectors.toList());

        } catch (IOException e) {
        	log.debug("IOException = {}", e.getMessage());
            throw new ExcelReaderFileException("엑셀파일 읽기에 실패했습니다. 파일을 확인해주세요.", e);
        } catch (NullPointerException e) {
        	log.debug("NullPointerException = {}", e.getMessage());
        	throw new ExcelReaderFileException("엑셀파일 읽기에 실패했습니다. 파일을 확인해주세요.");
        } catch (ExcelReaderFieldException e) {
        	log.debug("ExcelReaderFieldException = {}", e.getMessage());
        	throw new ExcelReaderFieldException(e.getMessage());
        } catch (Exception e) {
        	log.debug("Exception = {}", e.getMessage());
        	throw new ExcelReaderFileException("오류가 발생했습니다. 관리자에게 문의해주세요.");
        }
        
		return objectList;
	}

    /**
     * 해당 ROW에 있는 데이터가 하나라도 비어있으면 빈 ROW로 판단하고 해당 ROW는 PASS 시킨다
     * @param row
     * @return boolean
     */
    private boolean isPass(Row row) {
    	Iterator<Cell> iterator = row.cellIterator();
    	
    	if(!iterator.hasNext()) {
    		return false;
    	}
    	
    	while(iterator.hasNext()) {
    		String cellValue = ExcelUtils.getValue(iterator.next());
    		if(StringUtils.isEmpty(cellValue)) {
    			return false;
    		}
    		
    	}
    	
    	return true;
    }
    
    /**
     * 해당 ROW에 있는 데이터가 모두 비어있으면 빈 ROW로 판단하고 해당 ROW는 PASS 시킨다
     * @param row
     * @return T
     */
    private T setObjectMapping(Row row) throws Exception {
			return ExcelUtils.setObjectMapping(excelDto.newInstance(), row);
    }

	/**
	 * 스트림 내 try-catch 블럭 처리하는 wrap method
	 * @param
	 * @return T
	 */
	public static <T> T wrap(ExceptionSupplier<T> f) {
			try {
				return f.get();
			} catch (ExcelReaderFieldException e) {
				throw new ExcelReaderFieldException(e.getMessage());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	}
}
