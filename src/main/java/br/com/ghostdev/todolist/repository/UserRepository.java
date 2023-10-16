package br.com.ghostdev.todolist.repository;

import br.com.ghostdev.todolist.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByUsername(String name);
}
