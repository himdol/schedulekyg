package com.schedule.schedulekyg.utils.excel.dto;

import com.schedule.schedulekyg.utils.excel.annotation.ExcelColumn;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExgrExcelDto {
	
	@ExcelColumn (headerName = "체험명", columnName="exgrNm", col = 0)
	private String exgrNm;
	
	@ExcelColumn(headerName = "뷰티포인트 ID", columnName="ctmChcsNo", col = 1)
	private String ctmChcsNo;
	
	@ExcelColumn(headerName = "상품명", columnName="prdNm", col = 2)	
	private String prdNm;
	
	@ExcelColumn(headerName = "체험단기간", columnName="mngrTerm", col = 3)	
	private String mngrTerm;
	
	
	@ExcelColumn(headerName = "서비스시작일(YYYY-MM-DD)", columnName="mngrBgnDt", col = 4)
	private String mngrBgnDt;

}
