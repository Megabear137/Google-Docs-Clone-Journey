package com.notes.notes_app.database_tier.repository;

import com.notes.notes_app.database_tier.entity.Tag;
import com.notes.notes_app.database_tier.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByOwnerIdAndName(Long ownerId, String name);

}
