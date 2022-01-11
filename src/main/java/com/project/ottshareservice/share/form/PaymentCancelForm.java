package com.project.ottshareservice.share.form;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestForm2 {

    private String merchant_uid;

    private String Authorization;
}
