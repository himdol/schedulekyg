package com.schedule.schedulekyg.dto.common;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseDTO {
  //생성자
  private String createBy;
  //생성날짜
  private Timestamp createDate;
  //수정자
  private String modifyBy;
  //수정날짜
  private Timestamp modifyDate;
}
