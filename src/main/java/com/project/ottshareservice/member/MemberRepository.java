package com.project.ottshareservice.member;

import com.project.ottshareservice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Member findByNickname(String username);

    Member findByEmail(String useremail);
}
