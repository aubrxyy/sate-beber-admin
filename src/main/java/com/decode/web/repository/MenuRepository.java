package com.decode.web.repository;

import com.decode.web.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByTitle(String url);
    @Query("SELECT m FROM Menu m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Menu> searchMenus(String query);
}
