package com.schedule.schedulekyg.controller.menu;

import com.schedule.schedulekyg.dto.menu.CategoryDTO;
import com.schedule.schedulekyg.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequiredArgsConstructor
public class MenuRestController {

  private final MenuService menuService;

  @PostMapping("/menu/rest/category")
  public ResponseEntity<List<CategoryDTO>> getDataCategory() {
    return ResponseEntity.ok(menuService.getDataCategory());
  }
}
