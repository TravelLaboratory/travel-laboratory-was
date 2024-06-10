package site.travellaboratory.be.controller.auth.dto;

public record UserJoinRequest(
    String username,
    String password,
    String nickname
) {
}

