package site.travellaboratory.be.user.presentation._auth.response.userauthentication;

import site.travellaboratory.be.user.domain._auth.Token;

public record ReissueTokenResponse(
    Boolean isReissue
) {
    public static ReissueTokenResponse from(Token token) {
        boolean isReissue = token != null && token.getToken() != null && !token.getToken().isEmpty();
        return new ReissueTokenResponse(isReissue);
    }
}
