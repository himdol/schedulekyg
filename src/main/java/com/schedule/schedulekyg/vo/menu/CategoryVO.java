package com.schedule.schedulekyg.vo.menu;

import com.schedule.schedulekyg.vo.common.BaseVO;
import lombok.Data;

@Data
public class CategoryVO extends BaseVO {
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
