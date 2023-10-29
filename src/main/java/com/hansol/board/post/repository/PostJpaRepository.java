package com.hansol.board.post.repository;

import com.hansol.board.post.domain.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findByIdAndPassword(Long id, String password);

    Page<PostEntity> findByCodeAndParentPostIsNull(String code, Pageable pageable);

    @Query(value = "select max(p.id) from PostEntity p where p.secret=false and p.id < :id")
    Long findPrevId(Long id);

    @Query(value = "select min(p.id) from PostEntity p where p.secret=false and p.id > :id")
    Long findNextId(Long id);
}
