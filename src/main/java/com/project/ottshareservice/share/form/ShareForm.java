package com.project.ottshareservice.share.form;

import com.project.ottshareservice.domain.ContentType;
import com.project.ottshareservice.domain.Share;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ShareForm {

    private Long id;

    @NotBlank(message = "값이 있어야합니다.")
    @Length(max = 20, message = "20자를 넘을 수 없습니다.")
    private String title;

    @NotBlank(message = "값이 있어야합니다.")
    private String serviceName;

    @NotBlank(message = "값이 있어야합니다.")
    private String shareEmail;

    @NotBlank(message = "값이 있어야합니다.")
    private String sharePassword;

    @NotNull(message = "값이 있어야합니다.")
    private ContentType contentType;

    private String description;

    @NotNull(message = "값이 있어야합니다.")
    private Long dailyRate;

    @NotNull(message = "값이 있어야합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate shareFinishAt;

    @NotNull(message = "값이 있어야합니다.")
    @Min(value = 1, message = "모집인원은 최소 한명은 되야합니다.")
    @Max(value = 6, message = "모집인원은 6명을 넘을 수 없습니다.")
    private Long recruitmentCount;

    public ShareForm(Share share) {
        this.title = share.getTitle();
        this.serviceName = share.getServiceName();
        this.shareEmail = share.getShareEmail();
        this.sharePassword = share.getSharePassword();
        this.contentType = share.getContentType();
        this.description = share.getDescription();
        this.dailyRate = share.getDailyRate();
        this.shareFinishAt = share.getShareFinishAt();
        this.recruitmentCount = share.getRecruitmentCount();
    }
}
