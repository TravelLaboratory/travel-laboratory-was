package site.travellaboratory.be.article.presentation.response.writer;

public record ArticleUpdatePrivacyResponse(
    boolean isPrivate
) {
    public static ArticleUpdatePrivacyResponse from(boolean isPrivate) {
        return new ArticleUpdatePrivacyResponse(
            isPrivate
        );
    }
}
