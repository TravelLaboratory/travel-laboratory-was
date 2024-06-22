package site.travellaboratory.be.controller.oauth.dto;

public record OAuthJoinRequest (
        String profileNickname,
        String profileImage,
        String accountEmail,
        Boolean isAgreement
){
}
