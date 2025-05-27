package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.response.GoogleErrorResponse;
import code.rice.bowl.spaghetti.dto.response.GoogleTokenResponse;
import code.rice.bowl.spaghetti.dto.response.GoogleUserInfoResponse;
import code.rice.bowl.spaghetti.exception.InternalServerError;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {
    @Value("${google.oauth.client-id}")
    private String googleClientId;

    @Value("${google.oauth.client-secret}")
    private String googleClientSecret;

    @Value("${google.oauth.redirect-uri}")
    private String googleRedirectUri;

    private final RestTemplate restTemplate;

    /**
     * auth code 를 이용하여 구글으로 부터 액세스 토큰 발급.
     * @param authCode  auth code
     * @return          GoogleTokenResponse
     */
    public GoogleTokenResponse getAccessToken(String authCode) {
        String tokenUri = "https://oauth2.googleapis.com/token";

        // 1. 인증 코드로 access 코드 요청하기
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "code=" + authCode +
                "&client_id=" + googleClientId +
                "&client_secret=" + googleClientSecret +
                "&redirect_uri=" + googleRedirectUri +
                "&grant_type=authorization_code";


        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<GoogleTokenResponse> response = restTemplate.exchange(
                    tokenUri,
                    HttpMethod.POST,
                    request,
                    GoogleTokenResponse.class);

            GoogleTokenResponse result = response.getBody();

            if (result != null) {
                return result;
            } else {
                throw new InvalidRequestException("Check your auth code");
            }
        } catch (HttpClientErrorException e) {
            // error code 400 : auth_code error or redirect url error
            // else : client id error or user secret error
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                String message = e.getResponseBodyAsString();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                try {
                    // 리턴 값을 파싱.
                    GoogleErrorResponse response =
                            objectMapper.readValue(message, GoogleErrorResponse.class);

                    // 에러 문에 error 필드가 없는 경우 -> 구글에서 뭔가 수정 함.
                    if (response.error.isEmpty()) {
                        throw new InternalServerError("Invalid GoogleError, error is empty");
                    }

                    // 리다이렉트 문 때문에 발생한 경우. 500 에러
                    // 그 외 일괄적으로 사용자 잘 못으로 리턴.
                    if (response.error.startsWith("redirect")) {
                        throw new InternalServerError("Redirect error");
                    } else {
                        throw new InvalidRequestException("Check your auth code");
                    }

                } catch (JsonProcessingException ex) {
                    // 파싱 실패 -> 구글에서 리턴하는 데이터 구조 변경.
                    // 500 에러 예정
                    throw new InternalServerError("Invalid GoogleError, format change.");
                }

            } else {
                throw new InternalServerError("Env error");
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error : google");
        }
    }

    /**
     * access token 으로 사용자 이메일 정보를 가져옴.
     * @param accessToken   현재 사용자의 access token
     * @return              요청한 사용자의 이메일.
     */
    public String getGoogleEmail(String accessToken) {
        String infoUri = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<GoogleUserInfoResponse> res = restTemplate.exchange(
                    infoUri,
                    HttpMethod.GET,
                    request,
                    GoogleUserInfoResponse.class
            );

            GoogleUserInfoResponse info = res.getBody();

            if (info != null) {
                return info.getEmail();
            } else {
                throw new RuntimeException("Unexpected error : google info");
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error : google info");
        }
    }
}
