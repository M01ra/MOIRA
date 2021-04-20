package MakeUs.Moira.util.token;

public interface TokenProvider {
    String getProviderName();
    String getUserSocialId(String accessToken);
}
