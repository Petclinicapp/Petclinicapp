package bootcamp.petclinic.dto.register;

import bootcamp.petclinic.enums.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 20, message = "Username must be between {min} and {max} characters long")
    private String username;

    @NotBlank(message = "Firstname is mandatory")
    private String firstname;

    @NotBlank(message = "Lastname is mandatory")
    private String lastname;

    @Email(message = "Email is not valid",
            regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @ToString.Exclude
    @NotBlank(message = "Password is mandatory")
//    @Pattern(message = "Password must include at least one uppercase letter", regexp = ".*[A-Z].*")
//    @Pattern(message = "Password must include at least one lowercase letter", regexp = ".*[a-z].*")
//    @Pattern(message = "Password must include at least one number", regexp = ".*\\d.*")
//    @Pattern(message = "Password must include at least one special character @,$,!,%,*,?,&,#,^", regexp = ".*[@$!%*?&#^].*")
//    @Size(min = 8, message = "Password must be at least {min} characters long")
    private String password;

    private Roles role;

    public RegisterRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}