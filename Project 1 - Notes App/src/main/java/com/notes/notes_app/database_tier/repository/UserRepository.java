package com.notes.notes_app.database_tier.repository;

import com.notes.notes_app.database_tier.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
