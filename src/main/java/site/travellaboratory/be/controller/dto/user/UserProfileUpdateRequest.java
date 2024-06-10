package site.travellaboratory.be.controller.dto.user;

public record UserProfileUpdateRequest (
        String nickname,
        String introduce
){
}
