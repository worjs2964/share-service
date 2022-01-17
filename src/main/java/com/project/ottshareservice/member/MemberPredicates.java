package com.project.ottshareservice.member;

import com.project.ottshareservice.domain.Keyword;
import com.project.ottshareservice.domain.QMember;
import com.querydsl.core.types.Predicate;

import java.util.Set;

public class MemberPredicates {

    public static Predicate findByKeywords
            (Set<Keyword> keywords) {
        QMember member = QMember.member;
        return member.keywords.any().in(keywords);
    }
}
