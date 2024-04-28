package net.guides.springboot.registrationlogindemo.service;

import net.guides.springboot.registrationlogindemo.dto.UserDto;
import net.guides.springboot.registrationlogindemo.entity.Role;
import net.guides.springboot.registrationlogindemo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
    
    UserDto getUserById(long userId);

    List<Role> getAllRoles();

    void updateUserRole(Long userId, Long roleId);

    void deleteUser(Long userId);

    void updateUserRoles(Long userId, List<Long> roleIds);


    User get(Long id);

    Optional<Object> findById(Long id);
    Optional<User> findByEmail(String email);
    void updateUserProfile(User user, String newName);
    void changeUserPassword(User user, String newPassword);

    List<User> findAllDrivers();

    User findUserById(Long driverId);
}
