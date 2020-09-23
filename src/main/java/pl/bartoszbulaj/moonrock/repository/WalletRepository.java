package pl.bartoszbulaj.moonrock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.bartoszbulaj.moonrock.entity.WalletEntity;

public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
}
