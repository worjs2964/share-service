package com.project.ottshareservice.domain;

import com.project.ottshareservice.member.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Share {

    @Id @GeneratedValue
    @Column(name = "share_id")
    private Long id;

    private String title;

    private String serviceName;

    private String shareEmail;

    private String sharePassword;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Lob
    private String description;

    private Long dailyRate;

    private LocalDate shareFinishAt;

    private boolean recruiting = false;

    @Column(nullable = false)
    private Long recruitmentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member master;

    @ManyToMany
    private List<Member> members = new ArrayList<>();

    public boolean isMaster(Member member) {
        return this.master.equals(member);
    }

    public long getDday() {
        return ChronoUnit.DAYS.between(LocalDate.now(), this.shareFinishAt) + 1;
    }

    public Long getCost() {
        return getDday() * dailyRate;
    }

    public Long getTotalCost() {
        return (long) (getCost() * 1.1);
    }

    public boolean canJoin() {
        return recruitmentCount > getJoinMemberCount();
    }

    public Long getJoinMemberCount() {
        return members.stream().count();
    }

    public boolean checkAlreadyJoinMember(Member member) {
        return members.contains(member);
    }

    public void join(Member member) {
        members.add(member);
    }



}
