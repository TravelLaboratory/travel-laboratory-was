package site.travellaboratory.be.presentation.user.dto;

public record UserDeleteResponse(
        Boolean isDelete
) {
    public static UserDeleteResponse from(Boolean isDelete) {
        return new UserDeleteResponse(isDelete);
    }
}
