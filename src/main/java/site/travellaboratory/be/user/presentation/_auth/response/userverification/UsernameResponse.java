package site.travellaboratory.be.user.presentation._auth.response.userverification;

public record UsernameResponse(
    Boolean available

) {
    public static UsernameResponse from(Boolean available) {
        return new UsernameResponse(
            available);
    }
}
