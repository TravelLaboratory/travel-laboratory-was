package site.travellaboratory.be.review.presentation.response.reader;

import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleLocationEntity;

public record ProfileReviewLocation(
    String placeId,
    String address,
    String city
) {
    public static ProfileReviewLocation from(ArticleLocationEntity articleLocationEntity) {
        return new ProfileReviewLocation(
            articleLocationEntity.getPlaceId(),
            articleLocationEntity.getAddress(),
            articleLocationEntity.getCity()
        );
    }
}
