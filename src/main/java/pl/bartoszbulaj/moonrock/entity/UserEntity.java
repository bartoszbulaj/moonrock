package pl.bartoszbulaj.moonrock.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private Boolean active;
    private String role;

    public UserEntity(String username, String password, Boolean active, String role) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.role = role;
    }
}
