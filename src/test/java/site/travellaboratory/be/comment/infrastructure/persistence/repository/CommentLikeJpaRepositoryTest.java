package site.travellaboratory.be.comment.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.Location;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.domain.enums.TravelCompanion;
import site.travellaboratory.be.article.domain.enums.TravelStyle;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.comment.domain.Comment;
import site.travellaboratory.be.comment.domain.CommentLike;
import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentEntity;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentLikeEntity;
import site.travellaboratory.be.common.presentation.config.JsonConfig;
import site.travellaboratory.be.review.domain.Review;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewEntity;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain._auth.enums.UserRole;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

@Import({JsonConfig.class})
@ActiveProfiles("h2-test")
@DataJpaTest
class CommentLikeJpaRepositoryTest {

    @Autowired
    private CommentLikeJpaRepository sut;

    @Autowired
    private TestEntityManager em;

    private UserEntity userEntity;
    private CommentEntity commentEntity;

    @BeforeEach
    void setUp() {
        this.userEntity = em.persist(
            UserEntity.from(User.builder()
                .username("userA@example.com")
                .nickname("userA")
                .introduce("introduce")
                .role(UserRole.USER)
                .password("password")
                .isAgreement(true)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build())
        );

        ArticleEntity articleEntity = em.persist(
            ArticleEntity.from(Article.builder()
                .user(userEntity.toModel())
                .title("title")
                .locations(List.of(
                    Location.builder()
                        .placeId("1")
                        .city("city")
                        .address("address")
                        .build()
                ))
                .startAt(LocalDate.now())
                .endAt(LocalDate.from(LocalDateTime.now().plusHours(1L)))
                .expense("10000원")
                .travelCompanion(TravelCompanion.from(TravelCompanion.ACTOR.getName()))
                .travelStyles(List.of(TravelStyle.fromDbValue(TravelStyle.ACTIVITY.getName())))
                .coverImgUrl(null)
                .status(ArticleStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build())
        );

        ReviewEntity reviewEntity = em.persist(ReviewEntity.from(
            Review.builder()
                .user(userEntity.toModel())
                .article(articleEntity.toModel())
                .title("title")
                .description("description")
                .status(ReviewStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        ));

        this.commentEntity = em.persist(CommentEntity.from(
            Comment.builder()
                .user(userEntity.toModel())
                .review(reviewEntity.toModel())
                .replyComment("Test Comment")
                .status(CommentStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        ));

        em.clear();
    }

    @Nested
    @DisplayName("CommentLikeJpaRepository Test")
    class CommentLikeJpaRepositoryTestCases {

        @DisplayName("유저 ID와 댓글 ID로 좋아요 조회")
        @Test
        void findByUserIdAndCommentId() {
            // given
            CommentLikeEntity commentLikeEntity = em.persistAndFlush(CommentLikeEntity.from(
                CommentLike.builder()
                    .user(userEntity.toModel())
                    .comment(commentEntity.toModel())
                    .status(CommentLikeStatus.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build()
            ));

            // when
            Optional<CommentLikeEntity> result = sut.findByUserIdAndCommentId(userEntity.getId(), commentEntity.getId());

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(commentLikeEntity.getId());
        }

        @DisplayName("좋아요 저장")
        @Test
        void save() {
            // given
            CommentLikeEntity commentLikeEntity = CommentLikeEntity.from(CommentLike.builder()
                .user(userEntity.toModel())
                .comment(commentEntity.toModel())
                .status(CommentLikeStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

            // when
            CommentLikeEntity savedCommentLike = sut.save(commentLikeEntity);

            // then
            assertThat(savedCommentLike.getId()).isNotNull();
            assertThat(savedCommentLike.getUserEntity().getId()).isEqualTo(userEntity.getId());
            assertThat(savedCommentLike.getCommentEntity().getId()).isEqualTo(commentLikeEntity.getCommentEntity().getId());
            assertThat(savedCommentLike.getCreatedAt()).isEqualTo(commentLikeEntity.getCreatedAt());
            assertThat(savedCommentLike.getUpdatedAt()).isAfterOrEqualTo(commentLikeEntity.getUpdatedAt());
            assertThat(savedCommentLike.getStatus()).isEqualTo(CommentLikeStatus.ACTIVE);
        }
    }
}