package site.travellaboratory.be.controller.user.dto;

public record UserProfileUpdateRequest (
        String nickname,
        String introduce
){
}
