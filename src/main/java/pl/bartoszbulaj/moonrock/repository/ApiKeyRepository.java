package pl.bartoszbulaj.moonrock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.bartoszbulaj.moonrock.entity.ApiKeyEntity;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {

    ApiKeyEntity getFirstByOwnerEquals(String owner);
}
