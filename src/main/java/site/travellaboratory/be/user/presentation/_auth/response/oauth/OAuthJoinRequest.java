package site.travellaboratory.be.user.presentation._auth.response.oauth;

public record OAuthJoinRequest (
        String profileNickname,
        String profileImage,
        String accountEmail,
        Boolean isAgreement
){
}
