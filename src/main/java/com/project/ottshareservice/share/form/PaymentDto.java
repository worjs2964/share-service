package com.project.ottshareservice.share.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentDto {

    private String impUid;

    private String impMerchantUid;

    private String impUrl;

    private Long impAmount;
}
