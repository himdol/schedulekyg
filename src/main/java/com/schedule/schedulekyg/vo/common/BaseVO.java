package com.schedule.schedulekyg.vo.common;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class BaseVO {
  //생성자
  private String createBy;
  //생성날짜
  private Timestamp createDate;
  //수정자
  private String modifyBy;
  //수정날짜
  private Timestamp modifyDate;
}
