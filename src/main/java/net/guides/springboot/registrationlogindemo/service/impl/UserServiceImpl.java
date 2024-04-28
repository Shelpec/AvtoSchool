package net.guides.springboot.registrationlogindemo.service.impl;

import lombok.AllArgsConstructor;
import net.guides.springboot.registrationlogindemo.dto.UserDto;
import net.guides.springboot.registrationlogindemo.entity.Role;
import net.guides.springboot.registrationlogindemo.entity.User;
import net.guides.springboot.registrationlogindemo.repository.RoleRepository;
import net.guides.springboot.registrationlogindemo.repository.UserRepository;
import net.guides.springboot.registrationlogindemo.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseGet(this::checkRoleExist);

        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(List.of(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }



    @Override
    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId).get();
        return mapToUserDto(user);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void updateUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + roleId));
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User get(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public Optional<Object> findById(Long id) {
        return Optional.of(userRepository.findById(id).get());
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());

        String[] str = user.getName().split(" ");

        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }
    @Override
    public void updateUserRoles(Long userId, List<Long> rolesIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        List<Role> newRoles = rolesIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role Not Found")))
                .collect(Collectors.toList()); // Собираем в List вместо Set

        user.setRoles(newRoles);
        userRepository.save(user);
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public void updateUserProfile(User user, String newName) {
        user.setName(newName);
        userRepository.save(user);
    }
    @Override
    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(newPassword); // Предполагается, что newPassword уже зашифрован
        userRepository.save(user);
    }
    @Override
    public List<User> findAllDrivers(){
        return userRepository.findByRoles_Name("ROLE_DRIVER");
    }
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

}
