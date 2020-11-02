package pl.bartoszbulaj.moonrock.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.bartoszbulaj.moonrock.entity.ApiKeyEntity;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {

	Optional<ApiKeyEntity> getFirstByOwnerEquals(String owner);
}
