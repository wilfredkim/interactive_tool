package com.interactivetool.wilfred.interactivetool.controllers;

import com.interactivetool.wilfred.interactivetool.entity.Menu;
import com.interactivetool.wilfred.interactivetool.services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("api/v1.0/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<Menu> save(@RequestBody Menu menu) {
        return ResponseEntity.ok(menuService.save(menu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Menu> update(@PathVariable Long id, @RequestBody Menu menu) {
        return ResponseEntity.ok(menuService.update(id, menu));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> get(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.get(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(menuService.findAll());
    }
}
