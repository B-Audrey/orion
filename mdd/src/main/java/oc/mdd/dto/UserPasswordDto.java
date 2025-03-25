package oc.mdd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPasswordDto {
    @NotEmpty(message = "password is required")
    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password is too short")
    private String newPassword;

    @NotEmpty(message = "password is required")
    @NotBlank(message = "password is required")
    private String actualPassword;
}
