package pl.bartoszbulaj.moonrock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.bartoszbulaj.moonrock.entity.PositionEntity;

public interface PositionRepository extends JpaRepository<PositionEntity, Long> {
}
