package site.travellaboratory.be.review.presentation.response.reader;

import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleLocationEntity;

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
