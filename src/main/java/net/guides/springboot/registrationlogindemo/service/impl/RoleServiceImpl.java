package net.guides.springboot.registrationlogindemo.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.guides.springboot.registrationlogindemo.entity.Role;
import net.guides.springboot.registrationlogindemo.repository.UserRepository;
import net.guides.springboot.registrationlogindemo.service.RoleService;
import org.apache.catalina.User;
import org.springframework.instrument.classloading.ResourceOverridingShadowingClassLoader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final UserRepository userRepository;

}
