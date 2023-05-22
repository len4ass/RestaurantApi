package ru.len4ass.api.controllers;

import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.len4ass.api.models.menu.Menu;
import ru.len4ass.api.services.MenuService;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    @Resource
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("")
    public ResponseEntity<Menu> getMenu() {
        return ResponseEntity.ok(menuService.getMenu());
    }
}
