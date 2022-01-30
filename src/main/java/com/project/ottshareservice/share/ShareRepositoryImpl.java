package com.project.ottshareservice.share;

import com.project.ottshareservice.domain.*;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.project.ottshareservice.domain.QShare.share;

@RequiredArgsConstructor
public class ShareRepositoryImpl implements ShareRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Share> findByKeyword(String keyword, Pageable pageable) {
        QueryResults<Share> shareQueryResults = queryFactory.select(share).distinct()
                .from(share)
                .where(share.visible.isTrue()
                        .and(share.title.containsIgnoreCase(keyword))
                        .or(share.serviceName.containsIgnoreCase(keyword))
                        .or(share.keywords.any().keyword.containsIgnoreCase(keyword))
                        .and(share.shareFinishAt.after(LocalDate.now().minusDays(1)))
                        .and(share.recruiting.isTrue()))
                .orderBy(share.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(shareQueryResults.getResults(), pageable, shareQueryResults.getTotal());
    }

    @Override
    public Page<Share> findByType(ContentType type, Pageable pageable) {
        QueryResults<Share> shareQueryResults = queryFactory.select(share).distinct()
                .from(share)
                .where(share.visible.isTrue()
                        .and(share.contentType.eq(type))
                        .and(share.shareFinishAt.after(LocalDate.now().minusDays(1)))
                        .and(share.recruiting.isTrue()))
                .orderBy(share.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(shareQueryResults.getResults(), pageable, shareQueryResults.getTotal());
    }

    @Override
    public List<Share> findNew12() {
        List<Share> fetch = queryFactory.selectFrom(share)
                .where(share.visible.isTrue()
                        .and(share.shareFinishAt.after(LocalDate.now().minusDays(1)))
                        .and(share.recruiting.isTrue()))
                .orderBy(share.id.desc())
                .distinct()
                .limit(12)
                .fetch();
        return fetch;
    }

    @Override
    public List<Share> findByMembersIn(Member member) {
        List<Share> fetch = queryFactory.selectFrom(share).distinct()
                .where(share.members.contains(member))
                .leftJoin(share.members, QMember.member).fetchJoin()
                .orderBy(share.id.desc())
                .limit(12)
                .fetch();
        return fetch;
    }

}
