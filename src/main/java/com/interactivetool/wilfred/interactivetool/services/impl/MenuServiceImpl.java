package com.interactivetool.wilfred.interactivetool.services.impl;

import com.interactivetool.wilfred.interactivetool.entity.Menu;
import com.interactivetool.wilfred.interactivetool.repository.MenuRepository;
import com.interactivetool.wilfred.interactivetool.services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    @Override
    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public Menu update(Long id, Menu updateMenu) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
        menu.setMenuCode(updateMenu.getMenuCode());
        menu.setTitle(updateMenu.getTitle());
        menu.setParentMenuId(updateMenu.getParentMenuId());
        menu.setDisplayOrder(updateMenu.getDisplayOrder());
        menu.setIsActive(updateMenu.getIsActive());
        menu.setIsRoot(updateMenu.getIsRoot());
        menu.setIsBack(updateMenu.getIsBack());
        menu.setMenuType(updateMenu.getMenuType());
        menu.setEventId(updateMenu.getEventId());
        return menuRepository.save(menu);
    }

    @Override
    public void delete(Long id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
        menuRepository.deleteById(menu.getId());
    }

    @Override
    public Menu get(Long id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
        return menu;
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }
}
