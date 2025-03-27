package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.response.GoogleTokenResponse;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class GoogleLoginService {
    @Value("${google.oauth.client-id}")
    private String googleClientId;

    @Value("${google.oauth.client-secret}")
    private String googleClientSecret;

    @Value("${google.oauth.redirect-uri}")
    private String googleRedirectUri;

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
            ResponseEntity<GoogleTokenResponse> response = new RestTemplate().exchange(
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
            throw new InvalidRequestException("Check your auth code");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while getting token from google");
        }
    }

    /**
     * access token 으로 사용자 이메일 정보를 가져옴.
     * @param accessToken   현재 사용자의 access token
     * @return              요청한 사용자의 이메일.
     */
    public String getGoogleEmail(String accessToken) {
        return "";
    }
}
