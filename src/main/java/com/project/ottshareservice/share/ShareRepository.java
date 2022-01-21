package com.project.ottshareservice.share;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Share;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long>, ShareRepositoryCustom {

    @EntityGraph(attributePaths = {"keywords"})
    Share findWithKeywordById(Long id);

    @EntityGraph(attributePaths = {"keywords", "members"})
    List<Share> findByMaster(Member member);
}
