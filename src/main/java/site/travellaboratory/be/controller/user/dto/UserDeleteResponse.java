package site.travellaboratory.be.controller.user.dto;

public record UserDeleteResponse(
        Boolean isDelete
) {
    public static UserDeleteResponse from(Boolean isDelete) {
        return new UserDeleteResponse(isDelete);
    }
}
