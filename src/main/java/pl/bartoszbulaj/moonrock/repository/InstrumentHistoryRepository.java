package pl.bartoszbulaj.moonrock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.bartoszbulaj.moonrock.entity.InstrumentHistoryEntity;

@Repository
public interface InstrumentHistoryRepository extends JpaRepository<InstrumentHistoryEntity, Long> {

	List<InstrumentHistoryEntity> findAllBySymbol(String symbol);
}
