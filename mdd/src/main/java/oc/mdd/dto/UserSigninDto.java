package oc.mdd.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserSigninDto {

    @Email(message = "email is not valid")
    @NotEmpty(message = "email is required")
    @NotBlank( message = "email is required")
    private String email;

    @NotEmpty(message = "name is required")
    @NotBlank(message = "name is required")
    @Size(min = 2, message = "name is too short")
    private String name;

    @NotEmpty(message = "password is required")
    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password is too short")
    private String password;

}

