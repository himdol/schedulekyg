package com.schedule.schedulekyg.dto.menu;

import com.schedule.schedulekyg.dto.common.BaseDTO;
import lombok.Data;

@Data
public class CategoryDTO extends BaseDTO{

  //카테고리 아이디
  private String categoryId;
  //카테고리 이름
  private String categoryName;
  //뎁스
  private int depth;
  //부모 카테고리 아이디
  private String parent;
  //정렬
  private String sort;
  //경로
  private String categoryPath;
}
