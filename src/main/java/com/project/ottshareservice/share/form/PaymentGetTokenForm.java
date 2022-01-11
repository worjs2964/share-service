package com.project.ottshareservice.share.form;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentGetTokenForm {

    private String imp_key;

    private String imp_secret;
}
