package site.travellaboratory.be.presentation.auth.dto.userverification;

public record UsernameResponse(
    Boolean available

) {
    public static UsernameResponse from(Boolean available) {
        return new UsernameResponse(
            available);
    }
}
