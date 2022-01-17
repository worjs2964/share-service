package com.project.ottshareservice.share;

import com.project.ottshareservice.domain.Share;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share, Long> {

    @EntityGraph(attributePaths = {"keywords"})
    Share findWithKeywordById(Long id);
}
