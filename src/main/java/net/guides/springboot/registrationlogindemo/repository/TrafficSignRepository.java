package net.guides.springboot.registrationlogindemo.repository;

import net.guides.springboot.registrationlogindemo.entity.TrafficSign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficSignRepository extends JpaRepository<TrafficSign, Long> {
}