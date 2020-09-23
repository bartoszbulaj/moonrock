package pl.bartoszbulaj.moonrock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;

public interface InstrumentHistoryRepository extends JpaRepository<InstrumentHistoryEntity, Long> {

}
