package site.travellaboratory.be.presentation.auth.dto.oauth;

public record OAuthJoinRequest (
        String profileNickname,
        String profileImage,
        String accountEmail,
        Boolean isAgreement
){
}
