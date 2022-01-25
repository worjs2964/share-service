package com.project.ottshareservice.share;

import com.project.ottshareservice.domain.*;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ShareRepositoryCustomImpl extends QuerydslRepositorySupport implements ShareRepositoryCustom {

    public ShareRepositoryCustomImpl() {
        super(Share.class);
    }

    @Override
    public Page<Share> findByKeyword(String keyword, Pageable pageable) {
        QShare share = QShare.share;
        JPQLQuery<Share> query = from(share).where(share.visible.isTrue()
                        .and(share.title.containsIgnoreCase(keyword))
                        .or(share.serviceName.containsIgnoreCase(keyword))
                        .or(share.keywords.any().keyword.containsIgnoreCase(keyword))
                        .and(share.shareFinishAt.after(LocalDate.now().minusDays(1)))
                        .and(share.recruiting.isTrue()))
                .orderBy(share.id.desc())
                .leftJoin(share.keywords, QKeyword.keyword1).fetchJoin()
                .distinct();

        JPQLQuery<Share> pageAbleQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Share> fetchResults = pageAbleQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public Page<Share> findByType(ContentType type, Pageable pageable) {
        QShare share = QShare.share;
        QueryResults<Share> fetchResults = from(share).where(share.visible.isTrue()
                        .and(share.contentType.eq(type))
                        .and(share.shareFinishAt.after(LocalDate.now().minusDays(1)))
                        .and(share.recruiting.isTrue()))
                .orderBy(share.id.desc())
                .leftJoin(share.keywords, QKeyword.keyword1).fetchJoin()
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }

    @Override
    public List<Share> findNew12() {
        QShare share = QShare.share;
        QueryResults<Share> fetchResults = from(share).where(share.visible.isTrue()
                        .and(share.shareFinishAt.after(LocalDate.now().minusDays(1)))
                        .and(share.recruiting.isTrue()))
                .orderBy(share.id.desc())
                .leftJoin(share.keywords, QKeyword.keyword1).fetchJoin()
                .distinct()
                .limit(12)
                .fetchResults();
        return fetchResults.getResults();
    }

    @Override
    public List<Share> findByMembersIn(Member member) {
        QShare share = QShare.share;

        QueryResults<Share> fetchResults = from(share).where(share.members.contains(member))
                .orderBy(share.id.desc())
                .leftJoin(share.members, QMember.member).fetchJoin()
                .leftJoin(share.keywords, QKeyword.keyword1).fetchJoin()
                .distinct()
                .fetchResults();
        return fetchResults.getResults();
    }

}
