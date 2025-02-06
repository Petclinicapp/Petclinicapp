package bootcamp.petclinic.dto.register;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class RegisterResponseDTO {

    private String id;
    private String username;
    private String email;
    private String token;
    private String message;
    private String accountNumber;

    public RegisterResponseDTO(String message) {
        this.message = message;
    }

    public RegisterResponseDTO(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public RegisterResponseDTO(String id, String message, String accountNumber) {
        this.id = id;
        this.message = message;
        this.accountNumber = accountNumber;
    }
}
