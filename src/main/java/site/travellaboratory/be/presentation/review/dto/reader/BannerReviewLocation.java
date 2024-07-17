package site.travellaboratory.be.presentation.review.dto.reader;

import site.travellaboratory.be.infrastructure.domains.article.entity.ArticleLocationEntity;

public record BannerReviewLocation(
    String placeId,
    String address,
    String city
) {
    public static BannerReviewLocation from(ArticleLocationEntity articleLocationEntity) {
        return new BannerReviewLocation(
            articleLocationEntity.getPlaceId(),
            articleLocationEntity.getAddress(),
            articleLocationEntity.getCity()
        );
    }
}
