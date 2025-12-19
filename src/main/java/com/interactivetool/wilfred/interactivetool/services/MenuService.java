package com.interactivetool.wilfred.interactivetool.services;

import com.interactivetool.wilfred.interactivetool.entity.Menu;

import java.util.List;

public interface MenuService {

    Menu save(Menu menu);

    Menu update(Long id,Menu menu);

    void delete(Long id);

    Menu get(Long id);

    List<Menu> findAll();
}
