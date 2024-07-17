package site.travellaboratory.be.user.presentation._auth.response.userunregistration;

public record UserUnregisterResponse(
        Boolean isDelete
) {
    public static UserUnregisterResponse from(Boolean isDelete) {
        return new UserUnregisterResponse(isDelete);
    }
}
