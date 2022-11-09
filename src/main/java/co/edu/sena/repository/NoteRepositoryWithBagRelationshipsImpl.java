package co.edu.sena.repository;

import co.edu.sena.domain.Note;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class NoteRepositoryWithBagRelationshipsImpl implements NoteRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Note> fetchBagRelationships(Optional<Note> note) {
        return note.map(this::fetchCourses);
    }

    @Override
    public Page<Note> fetchBagRelationships(Page<Note> notes) {
        return new PageImpl<>(fetchBagRelationships(notes.getContent()), notes.getPageable(), notes.getTotalElements());
    }

    @Override
    public List<Note> fetchBagRelationships(List<Note> notes) {
        return Optional.of(notes).map(this::fetchCourses).orElse(Collections.emptyList());
    }

    Note fetchCourses(Note result) {
        return entityManager
            .createQuery("select note from Note note left join fetch note.courses where note is :note", Note.class)
            .setParameter("note", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Note> fetchCourses(List<Note> notes) {
        return entityManager
            .createQuery("select distinct note from Note note left join fetch note.courses where note in :notes", Note.class)
            .setParameter("notes", notes)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
