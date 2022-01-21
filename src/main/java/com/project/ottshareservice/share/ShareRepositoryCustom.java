package com.project.ottshareservice.share;

import com.project.ottshareservice.domain.ContentType;
import com.project.ottshareservice.domain.Keyword;
import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Share;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Transactional(readOnly = true)
public interface ShareRepositoryCustom {

    Page<Share> findByKeyword(String keyword, Pageable pageable);

    Page<Share> findByType(ContentType type, Pageable pageable);

    List<Share> findNew12();

    List<Share> findByMembersIn(Member member);
}
