package bootcamp.petclinic.model;

import bootcamp.petclinic.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "token", unique = true, length = 1024)
    private String token;

    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "is_expired")
    private boolean isExpired;

    @Column(name = "is_revoked")
    private boolean isRevoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
