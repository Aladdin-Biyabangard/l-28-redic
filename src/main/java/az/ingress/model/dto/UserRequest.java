package az.ingress.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "User name cannot be empty")
    String userName;

    @Email
    String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(
            regexp = "^\\+?[1-9]\\d{1,14}$",
            message = "Invalid phone number. Must follow E.164 format (e.g., +994501234567)"
    )
    String phoneNumber;
}
