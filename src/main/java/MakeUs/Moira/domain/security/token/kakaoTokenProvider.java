package MakeUs.Moira.domain.security.token;


import MakeUs.Moira.advice.exception.AccessTokenInvalidException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class kakaoTokenProvider implements TokenProvider{

    private String providerName = "kakao";

    @Override
    public String getProviderName() {
        return this.providerName;
    }

    @Override
    public String getUserSocialId(String accessToken) {

        ResponseEntity<String> responseHttpEntity = requestByAccessToken(accessToken);

        if (responseHttpEntity.getStatusCode().value() != 200) {
            throw new AccessTokenInvalidException("AccessToken이 유효하지 않습니다.");
        }

        String response =responseHttpEntity.getBody();
        JSONObject jObject = new JSONObject(response);

        return String.valueOf(jObject.getInt("id"));
    }

    private ResponseEntity<String> requestByAccessToken(String accessToken) throws AccessTokenInvalidException{
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange("https://kapi.kakao.com/v1/user/access_token_info", HttpMethod.GET, requestEntity, String.class);
        return responseEntity;
    }
}
