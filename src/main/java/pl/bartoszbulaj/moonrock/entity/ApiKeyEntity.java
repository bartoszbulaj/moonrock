package pl.bartoszbulaj.moonrock.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String owner;
    private String apiPublicKey;
    private String apiSecretKey;

    public ApiKeyEntity(String owner, String apiPublicKey, String apiSecretKey) {
        this.owner = owner;
        this.apiPublicKey = apiPublicKey;
        this.apiSecretKey = apiSecretKey;
    }
}
