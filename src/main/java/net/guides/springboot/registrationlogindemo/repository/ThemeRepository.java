package net.guides.springboot.registrationlogindemo.repository;

import net.guides.springboot.registrationlogindemo.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {}
