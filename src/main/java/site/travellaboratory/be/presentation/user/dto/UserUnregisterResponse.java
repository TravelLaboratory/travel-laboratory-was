package site.travellaboratory.be.presentation.user.dto;

public record UserUnregisterResponse(
        Boolean isDelete
) {
    public static UserUnregisterResponse from(Boolean isDelete) {
        return new UserUnregisterResponse(isDelete);
    }
}
