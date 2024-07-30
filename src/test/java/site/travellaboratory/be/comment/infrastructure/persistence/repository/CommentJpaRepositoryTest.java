package site.travellaboratory.be.comment.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentEntity;
import site.travellaboratory.be.common.presentation.config.JsonConfig;
import site.travellaboratory.be.review.infrastructure.persistence.entity.ReviewEntity;

@Import({JsonConfig.class})
@ActiveProfiles("h2-test")
@DataJpaTest
class CommentJpaRepositoryTest {

    @Autowired
    private CommentJpaRepository sut;

    @Autowired
    private TestEntityManager em;

    @Nested
    @DisplayName("댓글 ID와 상태로 댓글 조회 - findByIdAndStatusIn - ")
    class findByIdAndStatusIn {

        @DisplayName("[Fail] 존재하지 않는 댓글 ID일 경우 - 빈 Entity 반환")
        @Test
        void test1() {
            //given
            Long commentId = 9999L; // 존재하지 않는 (유효하지 않은) 댓글 ID
            CommentEntity commentEntity = em.find(CommentEntity.class, commentId);

            //when
            Optional<CommentEntity> result = sut.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE));

            //then
            assertThat(commentEntity).isNull();
            assertThat(result).isEmpty();
        }

        @DisplayName("[Fail] 삭제된 댓글 ID일 경우 - 빈 Entity 반환")
        @Test
        void test2() {
            //given
            Long commentId = 1L;
            CommentEntity commentEntity = em.find(CommentEntity.class, commentId);


            //when
            Optional<CommentEntity> result = sut.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE));

            //then
            assertThat(commentEntity.getStatus()).isEqualTo(CommentStatus.INACTIVE);
            assertThat(result).isEmpty();
        }

        @DisplayName("[Success] 활성화 댓글 ID와 상태로일 경우 - CommentEntity 반환")
        @Test
        void test1000() {
            //given
            Long commentId = 2L;
            CommentEntity commentEntity = em.find(CommentEntity.class, commentId);

            //when
            Optional<CommentEntity> result = sut.findByIdAndStatusIn(commentEntity.getId(),
                List.of(CommentStatus.ACTIVE));

            //then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(commentEntity.getId());
            assertThat(result.get().getStatus()).isEqualTo(CommentStatus.ACTIVE);
        }
    }

    @Nested
    @DisplayName("후기 ID와 댓글 상태로 댓글 리스트 조회 (생성일자로 내림차순 정렬) - findByReviewEntityIdAndStatusOrderByCreatedAtDesc")
    class findByReviewEntityIdAndStatusOrderByCreatedAtDesc {

        @DisplayName("[Success] 공개 후기 ID에 대한 댓글 리스트 조회")
        @Test
        void test1000() {
            //given
            Long reviewId = 2L; // ACTIVE 리뷰 ID
            ReviewEntity reviewEntity = em.find(ReviewEntity.class, reviewId);
            Pageable pageable = PageRequest.of(0, 10);

            //when
            Page<CommentEntity> result = sut.findByReviewEntityIdAndStatusOrderByCreatedAtDesc(
                reviewEntity.getId(), CommentStatus.ACTIVE,
                pageable);

            //then
            assertThat(result).isNotEmpty();
            List<CommentEntity> comments = result.getContent();
            assertThat(comments).hasSize(2);
            assertThat(comments.get(0).getStatus()).isEqualTo(CommentStatus.ACTIVE);
            assertThat(comments.get(1).getStatus()).isEqualTo(CommentStatus.ACTIVE);
            assertThat(comments.get(0).getCreatedAt()).isAfterOrEqualTo(
                comments.get(1).getCreatedAt());
        }
    }

    @Nested
    @DisplayName("같은 후기에 대한 댓글 ID 리스트로 (댓글 관련 정보) 리스트 조회 - findAllByIdInFetchJoinUser")
    class findAllByIdInFetchJoinUser {

        @DisplayName("[Success] 댓글 ID 리스트로 (댓글 관련 정보) 리스트 조회 - 댓글 작성자 포함")
        @Test
        void test1000() {
            //given
            // commentId 1L은 삭제된 댓글
            List<Long> commentIds = List.of(1L, 2L, 3L);

            //when
            List<CommentEntity> result = sut.findAllByIdInFetchJoinUser(commentIds, CommentStatus.ACTIVE);

            //then
            assertThat(result).isNotEmpty();
            assertThat(result).hasSize(2);

            CommentEntity comment1 = result.get(0);
            CommentEntity comment2 = result.get(1);

            assertThat(comment1.getStatus()).isEqualTo(CommentStatus.ACTIVE);
            assertThat(comment2.getStatus()).isEqualTo(CommentStatus.ACTIVE);

            assertThat(comment1.getUserEntity()).isNotNull();
            assertThat(comment2.getUserEntity()).isNotNull();

            assertThat(comment1.getReviewEntity().getId()).isEqualTo(
                comment2.getReviewEntity().getId());
        }
    }
}