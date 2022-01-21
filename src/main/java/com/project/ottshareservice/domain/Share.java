package com.project.ottshareservice.domain;

import com.project.ottshareservice.member.UserAccount;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @NotBlank
    private String title;

    @NotBlank
    private String serviceName;

    @NotBlank
    private String shareEmail;

    @NotBlank
    private String sharePassword;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Lob
    private String description;

    @NotNull
    private Long dailyRate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate shareFinishAt;

    private boolean visible = false;

    private boolean recruiting = true;

    private boolean alreadyNotification = false;

    @Column(nullable = false)
    private Long recruitmentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member master;

    @ManyToMany
    private List<Member> members = new ArrayList<>();

    @ManyToMany
    private Set<Keyword> keywords = new HashSet<>();

    public boolean isMaster(Object member) {
        if (member == null) {
            return false;
        } else if (member.getClass() == Member.class) {
            return this.master.equals(member);
        } else if (member.getClass() == UserAccount.class) {
            return this.master.equals(((UserAccount) member).getMember());
        }
        return false;
    }

    public boolean checkAlreadyJoinMember(Object member) {
        if (member == null) {
            return false;
        } else if (member.getClass() == Member.class) {
            return this.members.contains(member);
        } else if (member.getClass() == UserAccount.class) {
            return this.members.contains(((UserAccount) member).getMember());
        }
        return false;
    }

    public long getRamainDays() {
        return ChronoUnit.DAYS.between(LocalDate.now(), this.shareFinishAt) + 1;
    }

    public Long getCost() {
        return getRamainDays() * dailyRate;
    }

    public Long getTotalCost() {
        return (long) (getCost() * 1.1);
    }

    public Long getJoinMemberCount() {
        return members.stream().count();
    }

    public boolean canJoin() {
        return recruitmentCount > getJoinMemberCount() && shareFinishAt.isAfter(LocalDate.now().minusDays(1)) && visible;
    }

    public void join(Member member) {
        members.add(member);
        master.plusPoint(getCost());
    }



}
