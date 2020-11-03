package pl.bartoszbulaj.moonrock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.bartoszbulaj.moonrock.entity.EmailSenderEntity;

@Repository
public interface EmailSenderRepository extends JpaRepository<EmailSenderEntity, Long> {

}
