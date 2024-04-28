package net.guides.springboot.registrationlogindemo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import net.guides.springboot.registrationlogindemo.entity.Role;

import java.util.List;

@Data
public class UserDto {
    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
    @NotEmpty(message = "Email should not be empty")
    private String email;

    @NotEmpty(message = "Passwors should not be empty")
    private String password;

    private List<Role> roles;

}
