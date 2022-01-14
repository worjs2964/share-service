package com.project.ottshareservice.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Keyword {

    @Id
    @GeneratedValue
    @Column(name = "keyword_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String keyword;
}
