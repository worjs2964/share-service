package com.project.ottshareservice.member;

import com.project.ottshareservice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemberRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Member findByNickname(String username);

    Member findByEmail(String useremail);
}
