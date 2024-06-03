package site.travellaboratory.be.controller.dto;

public record UserProfileUpdateRequest (
        String nickname,
        String introduce
){
}
