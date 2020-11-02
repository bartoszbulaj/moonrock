package pl.bartoszbulaj.moonrock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.bartoszbulaj.moonrock.entity.WalletEntity;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
}
