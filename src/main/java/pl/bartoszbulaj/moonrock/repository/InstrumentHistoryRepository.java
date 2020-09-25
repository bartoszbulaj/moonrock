package pl.bartoszbulaj.moonrock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;

public interface InstrumentHistoryRepository extends JpaRepository<InstrumentHistoryEntity, Long> {

	List<InstrumentHistoryEntity> findAllBySymbol(String symbol);
}
