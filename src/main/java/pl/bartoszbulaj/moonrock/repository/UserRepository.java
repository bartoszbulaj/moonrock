package pl.bartoszbulaj.moonrock.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.bartoszbulaj.moonrock.entity.UserEntity;

public interface UserRepository extends JpaRepository <UserEntity, Long> {

}
