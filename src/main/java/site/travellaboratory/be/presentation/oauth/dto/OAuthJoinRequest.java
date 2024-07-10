package site.travellaboratory.be.presentation.oauth.dto;

public record OAuthJoinRequest (
        String profileNickname,
        String profileImage,
        String accountEmail,
        Boolean isAgreement
){
}
