package com.schedule.schedulekyg.repository.menu;

import com.schedule.schedulekyg.vo.menu.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MenuRepository {
  List<CategoryVO> getDataCategory(String categoryRootId);

}
