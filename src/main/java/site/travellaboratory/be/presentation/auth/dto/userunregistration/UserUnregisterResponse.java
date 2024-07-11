package site.travellaboratory.be.presentation.auth.dto.userunregistration;

public record UserUnregisterResponse(
        Boolean isDelete
) {
    public static UserUnregisterResponse from(Boolean isDelete) {
        return new UserUnregisterResponse(isDelete);
    }
}
