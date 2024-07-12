package site.travellaboratory.be.presentation.article.dto.writer;

public record ArticleUpdatePrivacyResponse(
    boolean isPrivate
) {
    public static ArticleUpdatePrivacyResponse from(boolean isPrivate) {
        return new ArticleUpdatePrivacyResponse(
            isPrivate
        );
    }
}