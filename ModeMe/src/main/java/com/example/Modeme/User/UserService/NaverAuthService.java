package com.example.Modeme.User.UserService;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NaverAuthService {

    private static final String CLIENT_ID = "L2yk1g6sJC3EhfnnBVtN";
    private static final String CLIENT_SECRET = "uZxphyPvEZ";
    private static final String REDIRECT_URI = "http://localhost:9999/api/auth/naver/callback"; // ✅ 콜백 URL 수정

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 네이버로부터 Access Token 받기
     */
    public String getAccessToken(String code, String state) {
        String tokenUrl = "https://nid.naver.com/oauth2.0/token" +
                "?grant_type=authorization_code" +
                "&client_id=" + CLIENT_ID +
                "&client_secret=" + CLIENT_SECRET +
                "&code=" + code +
                "&state=" + state;

        ResponseEntity<String> response = restTemplate.getForEntity(tokenUrl, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("네이버 Access Token 요청 실패", e);
        }
    }

    /**
     * 네이버 사용자 정보 가져오기
     */
    public String getUserInfo(String accessToken) {
        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.path("response").get("email").asText();  // 📌 JSON에서 이메일 값만 추출
        } catch (Exception e) {
            throw new RuntimeException("네이버 사용자 정보 요청 실패", e);
        }
    }
}
