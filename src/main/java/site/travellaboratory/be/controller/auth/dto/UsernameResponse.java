package site.travellaboratory.be.controller.auth.dto;

public record UsernameResponse(
    Boolean available

) {
    public static UsernameResponse from(Boolean available) {
        return new UsernameResponse(
            available);
    }
}
