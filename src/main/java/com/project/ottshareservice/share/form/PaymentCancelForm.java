package com.project.ottshareservice.share.form;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentCancelForm {

    private String merchant_uid;
}
