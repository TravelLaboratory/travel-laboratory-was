package site.travellaboratory.be.presentation.review.dto.reader;

import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleLocationEntity;

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
