package site.travellaboratory.be.user.domain._auth.request;

public record OAuthJoinRequest (
        String profileNickname,
        String profileImgUrl,
        String accountEmail,
        Boolean isAgreement
){
}
