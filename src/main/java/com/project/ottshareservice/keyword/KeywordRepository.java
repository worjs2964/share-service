package com.project.ottshareservice.keyword;

import com.project.ottshareservice.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Keyword findByKeyword(String keyword);
}
