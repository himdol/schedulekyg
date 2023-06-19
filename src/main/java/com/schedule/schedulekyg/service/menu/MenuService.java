package com.schedule.schedulekyg.service.menu;

import com.schedule.schedulekyg.dto.menu.CategoryDTO;
import com.schedule.schedulekyg.repository.menu.MenuRepository;
import com.schedule.schedulekyg.utils.CommonConstants;
import com.schedule.schedulekyg.vo.menu.CategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;

  public List<CategoryDTO> getDataCategory() {
    List<CategoryVO> categoryVOList = menuRepository.getDataCategory(CommonConstants.CATEGORY_ROOT_ID);
    List<CategoryDTO> categoryDTOList = new ArrayList<>();
    CategoryDTO categoryDTO = null;
    for (CategoryVO categoryVO : categoryVOList) {
      categoryDTO = new CategoryDTO();
      categoryDTO.setCategoryId(categoryVO.getCategoryId());
      categoryDTO.setCategoryName(categoryVO.getCategoryName());
      categoryDTO.setDepth(categoryVO.getDepth());
      categoryDTO.setParent(categoryVO.getParent());
      categoryDTO.setCategoryPath(categoryVO.getCategoryPath());
      categoryDTO.setSort(categoryVO.getSort());
      categoryDTO.setCreateBy(categoryVO.getCreateBy());
      categoryDTO.setCreateDate(categoryVO.getCreateDate());
      categoryDTO.setModifyBy(categoryVO.getModifyBy());
      categoryDTO.setModifyDate(categoryVO.getModifyDate());
      categoryDTOList.add(categoryDTO);
    }
    return categoryDTOList;
  }


}
