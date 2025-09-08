package az.ingress.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@AllArgsConstructor
@Data
public class UserResponse {

    Long id;

    String userName;

    String email;

}
