package site.travellaboratory.be.user.presentation._auth.response.userverification;

public record UsernameResponse(
    Boolean isAvailable

) {
    public static UsernameResponse from(Boolean isAvailable) {
        return new UsernameResponse(
            isAvailable);
    }
}
