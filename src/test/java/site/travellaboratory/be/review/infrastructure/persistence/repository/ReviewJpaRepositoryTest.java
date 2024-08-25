package site.travellaboratory.be.review.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import site.travellaboratory.be.article.infrastructure.persistence.converter.TravelCompanionConverter;
import site.travellaboratory.be.article.infrastructure.persistence.converter.TravelStyleConverter;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.common.infrastructure.config.JpaConfig;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewEntity;

@Import({JpaConfig.class, TravelCompanionConverter.class, TravelStyleConverter.class})
@ActiveProfiles("h2-test")
@DataJpaTest
class ReviewJpaRepositoryTest {

    @Autowired
    private ReviewJpaRepository sut;

    @Autowired
    private TestEntityManager em;

    @Nested
    class findByArticleEntityAndStatusIn  {
        @DisplayName("[Fail] 삭제된 후기 단일 조회 - 여행계획[ArticleEntity]과_원하는_후기_상태[ReviewStatus]로_후기_조회")
        @Test
        void test1() {
            //given
            Long articleId = 2L;
            ArticleEntity articleEntity = em.find(ArticleEntity.class, articleId);

            //when
            Optional<ReviewEntity> result = sut.findByArticleEntityAndStatus(
                articleEntity, ReviewStatus.INACTIVE);

            //then
            assertThat(result).isEmpty();
        }

        @DisplayName("[Success] 공개 후기 단일 조회 - 여행계획[ArticleEntity]과_원하는_후기_상태[ReviewStatus]로_후기_조회")
        @Test
        void test2() {
            //given
            Long articleId = 2L;
            Long reviewId = 2L;
            ArticleEntity articleEntity = em.find(ArticleEntity.class, articleId);

            //when
            Optional<ReviewEntity> result = sut.findByArticleEntityAndStatus(
                articleEntity, ReviewStatus.ACTIVE);

            //then
            assertThat(result).isPresent();
            ReviewEntity reviewEntity = result.get();
            assertThat(reviewEntity.getId()).isEqualTo(reviewId);
            assertThat(reviewEntity.getStatus()).isEqualTo(ReviewStatus.ACTIVE);
        }
    }

    @Nested
    class findByIdAndStatus {
        @DisplayName("[Fail] 존재하지 않는 후기 조회 - 리뷰 ID와_원하는_후기_상태[ReviewStatus]로_후기_조회")
        @Test
        void test1() {
            //given
            Long reviewId = 999L; // 존재하지 않는 ID

            //when
            Optional<ReviewEntity> result = sut.findByIdAndStatus(reviewId, ReviewStatus.ACTIVE);

            //then
            assertThat(result).isEmpty();
        }

        @DisplayName("[Success] 공개 후기 단일 조회 - 리뷰 ID와_원하는_후기_상태[ReviewStatus]로_후기_조회")
        @Test
        void test2() {
            //given
            Long reviewId = 2L;

            //when
            Optional<ReviewEntity> result = sut.findByIdAndStatus(reviewId, ReviewStatus.ACTIVE);

            //then
            assertThat(result).isPresent();
            ReviewEntity reviewEntity = result.get();
            assertThat(reviewEntity.getId()).isEqualTo(reviewId);
            assertThat(reviewEntity.getStatus()).isEqualTo(ReviewStatus.ACTIVE);
        }

        @DisplayName("[Fail] 삭제된 후기 조회 - 리뷰 ID와_원하는_후기_상태[ReviewStatus]로_후기_조회")
        @Test
        void test3() {
            //given
            Long reviewId = 3L; // 존재하지 않는 상태

            //when
            Optional<ReviewEntity> result = sut.findByIdAndStatus(reviewId, ReviewStatus.ACTIVE);

            //then
            assertThat(result).isEmpty();
        }
    }
}