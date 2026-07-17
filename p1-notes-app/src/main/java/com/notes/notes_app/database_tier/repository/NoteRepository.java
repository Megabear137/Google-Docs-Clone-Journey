package com.notes.notes_app.database_tier.repository;

import com.notes.notes_app.database_tier.entity.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @EntityGraph(attributePaths = "tags")
    List<Note> findByOwnerId(Long ownerId);

    @EntityGraph(attributePaths = "tags")
    @Query( "SELECT n FROM Note n " +
            "WHERE n.owner.id = :ownerId " +
            "AND ( LOWER(n.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "      OR (LOWER(n.body) LIKE LOWER(CONCAT('%', :search, '%'))))"
          )
    List<Note> findBySearch(@Param("ownerId") Long owner_id, @Param("search") String search);

}
