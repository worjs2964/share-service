package com.project.ottshareservice.share;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Share;
import com.project.ottshareservice.member.CurrentMember;
import com.project.ottshareservice.share.form.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    @Value(value = "${imp.key}")
    private String key;
    @Value(value = "${imp.secret}")
    private String secret;

    private final ShareService shareService;
    private final ShareRepository shareRepository;
    private RestTemplate restTemplate = new RestTemplate();


    @PostMapping("/share/{shareId}/payment")
    public Boolean joinShare(@CurrentMember Member member, @RequestBody PaymentDto paymentDto,
                             @PathVariable Long shareId) {
        Share share = shareRepository.findById(shareId).get();
        if (share.isMaster(member) || share.checkAlreadyJoinMember(member) || !share.isRecruiting() || !share.canJoin()) {
            paymentCancel(paymentDto.getImpUid());
            return false;
        } else if (!getPaymentPrice(paymentDto.getImpUid()).equals(share.getTotalCost().toString())) {
            paymentCancel(paymentDto.getImpUid());
            return false;
        }
        shareService.join(share, member);
        return true;
    }

    private void paymentCancel(String impUid) {
        cancelReqWithToken(impUid, getAccessToken());
    }

    private String getPaymentPrice(String impUid) {
        return getPaymentPriceWithToken(impUid, getAccessToken()).getResponse().getAmount();
    }

    private PaymentCostDto getPaymentPriceWithToken(String impUid, String accessToken) {
        URI uri = getUri("https://api.iamport.kr", "/payments/" + impUid);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", accessToken);

        HttpEntity<Object> request = new HttpEntity<>(httpHeaders);


        return restTemplate.exchange(uri, HttpMethod.GET, request, PaymentCostDto.class).getBody();
    }

    private void cancelReqWithToken(String impUid, String accessToken) {
        URI uri = getUri("https://api.iamport.kr", "/payments/cancel");
        CancelForm form = CancelForm.builder()
                .impUid(impUid)
                .build();

        HttpEntity<CancelForm> request = formWithHeader(accessToken, form);
        restTemplate.postForObject(uri, request, Object.class);
    }

    private String getAccessToken() {
        URI uri = getUri("https://api.iamport.kr", "/users/getToken");
        GetTokenForm getTokenForm = GetTokenForm.builder()
                .impKey(key)
                .impSecret(secret)
                .build();
        TokenDto tokenDto = restTemplate.postForObject(uri, getTokenForm, TokenDto.class);
        return tokenDto.getResponse().getAccessToken().toString();
    }

    private<T> HttpEntity<T> formWithHeader(String accessToken, T form) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", accessToken);

        return new HttpEntity<T>(form, httpHeaders);
    }

    private URI getUri(String uri, String path) {
        return UriComponentsBuilder.fromUriString(uri)
                .path(path)
                .encode()
                .build()
                .toUri();

    }
}
