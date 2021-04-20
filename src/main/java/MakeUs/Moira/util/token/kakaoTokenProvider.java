package MakeUs.Moira.util.token;


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

        String response = responseHttpEntity.getBody();
        JSONObject jObject = new JSONObject(response);

        return String.valueOf(jObject.getInt("id"));
    }

    private ResponseEntity<String> requestByAccessToken(String accessToken){
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange("https://kapi.kakao.com/v1/user/access_token_info", HttpMethod.GET, requestEntity, String.class);
        return responseEntity;
    }
}
