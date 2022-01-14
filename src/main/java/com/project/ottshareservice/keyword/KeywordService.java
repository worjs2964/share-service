package com.project.ottshareservice.keyword;

import com.project.ottshareservice.domain.Keyword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public Keyword findOrCreateNew(String keyword) {
        Keyword findKeyword = keywordRepository.findByKeyword(keyword);
        if (findKeyword == null) {
            Keyword newKeyword = Keyword.builder()
                    .keyword(keyword)
                    .build();

            return keywordRepository.save(newKeyword);
        } else {
            return findKeyword;
        }
    }
}
