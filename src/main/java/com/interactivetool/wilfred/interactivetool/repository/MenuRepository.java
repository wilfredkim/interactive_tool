package com.interactivetool.wilfred.interactivetool.repository;

import com.interactivetool.wilfred.interactivetool.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m FROM Menu m WHERE m.isRoot = true AND m.isActive = true")
    Menu findRootMenu();

    List<Menu> findActiveByParentMenuId(Long id);
    
    @Query("SELECT m FROM Menu m WHERE m.parentMenuId = ?1 AND m.isActive = true ORDER BY m.displayOrder")
    List<Menu> findByParentMenuIdAndIsActiveOrderByDisplayOrder(Long parentMenuId);
    
    @Query("SELECT m FROM Menu m WHERE m.parentMenuId = ?1 AND m.displayOrder = ?2 AND m.isActive = true")
    Menu findByParentMenuIdAndDisplayOrder(Long parentMenuId, Integer displayOrder);
    
    @Query("SELECT m FROM Menu m WHERE m.menuCode = ?1 AND m.isActive = true")
    Menu findByMenuCode(String menuCode);
}
