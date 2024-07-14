package site.travellaboratory.be.presentation.review.dto.reader;

import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleLocationJpaEntity;

public record BannerReviewLocation(
    String placeId,
    String address,
    String city
) {
    public static BannerReviewLocation from(ArticleLocationJpaEntity articleLocationJpaEntity) {
        return new BannerReviewLocation(
            articleLocationJpaEntity.getPlaceId(),
            articleLocationJpaEntity.getAddress(),
            articleLocationJpaEntity.getCity()
        );
    }
}
