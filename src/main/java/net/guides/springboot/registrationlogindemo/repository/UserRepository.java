package net.guides.springboot.registrationlogindemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import net.guides.springboot.registrationlogindemo.entity.User;

import java.util.List;
import java.util.Optional;

public interface
UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByName(String username);

    List<User> findByRoles_Name(String roleDriver);
    Optional<User> findUserById(Long id);
}
