package site.travellaboratory.be.presentation.review.dto.reader;

import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleLocationJpaEntity;

public record ProfileReviewLocation(
    String placeId,
    String address,
    String city
) {
    public static ProfileReviewLocation from(ArticleLocationJpaEntity articleLocationJpaEntity) {
        return new ProfileReviewLocation(
            articleLocationJpaEntity.getPlaceId(),
            articleLocationJpaEntity.getAddress(),
            articleLocationJpaEntity.getCity()
        );
    }
}
