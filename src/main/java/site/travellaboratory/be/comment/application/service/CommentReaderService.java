package site.travellaboratory.be.comment.application.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.comment.domain.enums.CommentLikeStatus;
import site.travellaboratory.be.comment.domain.enums.CommentStatus;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentEntity;
import site.travellaboratory.be.comment.infrastructure.persistence.entity.CommentLikeEntity;
import site.travellaboratory.be.comment.infrastructure.persistence.repository.CommentJpaRepository;
import site.travellaboratory.be.comment.infrastructure.persistence.repository.CommentLikeJpaRepository;
import site.travellaboratory.be.comment.presentation.response.reader.CommentLikeCount;
import site.travellaboratory.be.comment.presentation.response.reader.CommentReadPaginationResponse;
import site.travellaboratory.be.comment.presentation.response.reader.CommentReadResponse;
import site.travellaboratory.be.common.error.ErrorCodes;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.review.domain.enums.ReviewStatus;
import site.travellaboratory.be.review.infrastructure.persistence.repository.ReviewJpaRepository;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class CommentReaderService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final CommentLikeJpaRepository commentLikeJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional(readOnly = true)
    public CommentReadPaginationResponse readCommentsPagination(
        Long userId, Long reviewId, int page, int size
    ) {
        // 유효하지 않은 후기를 조회할 경우
        validateReview(reviewId);

        // 유효한 유저인지 조회
        User user = getUserById(userId);

        // 후기 리스트 조회 (N+1 쿼리 해결)
        // (1) 페이징 처리
        Page<CommentEntity> commentPage = commentJpaRepository.findByReviewEntityIdAndStatusOrderByCreatedAtDesc(reviewId,
            CommentStatus.ACTIVE, PageRequest.of(page, size));

        // (2) 좋아요 체크, 좋아요 수 알기 위해
        List<Long> commentIds = commentPage.getContent().stream()
            .map(CommentEntity::getId) // comment table id로 변환
            .toList();

        // (3)-1 좋아요 수
        List<CommentLikeCount> commentLikeCounts = commentLikeJpaRepository.countByCommentIdsAndStatusGroupByCommentId(
            commentIds,
            CommentLikeStatus.ACTIVE).stream().map(result -> new CommentLikeCount(
            (Long) result[0], (Long) result[1])).toList();

        // (3)-2 좋아요 수 개수 Map<commentId, likeCount> 변환
        Map<Long, Long> likeCounts = commentLikeCounts.stream()
            .collect(Collectors.toMap(
                CommentLikeCount::commentId,
                CommentLikeCount::likeCount
            ));

        // (4) userId와 commentId 들를 기준으로 데이터 리스트 뽑아오기 (N+1 해결)
        List<CommentLikeEntity> commentLikeJpaEntities = commentLikeJpaRepository.findAllByUserIdAndCommentIdInAndStatusFetchJoinComment(
            userId, commentIds,
            CommentLikeStatus.ACTIVE);
        // (4)-2 좋아요 체크 Map<commentId, UserLikeCommentStatus> 변환
        Map<Long, CommentLikeStatus> isLikes = commentLikeJpaEntities.stream()
            .collect(Collectors.toMap(
                commentLikeEntity -> commentLikeEntity.getCommentEntity().getId(),
                CommentLikeEntity::getStatus
            ));

        // (4) 댓글과 연관된 유저 정보를 포함한 응답 생성
        List<CommentEntity> commentsWithUsers = commentJpaRepository.findAllByIdInFetchJoinUser(commentIds, CommentStatus.ACTIVE);

        // (5) 최종 데이터 반환
        List<CommentReadResponse> comments = commentsWithUsers.stream().map(comment -> {
                // isLikes 에서 commentId 가 있고, 그게 ACTIVE 라면 true
                boolean isLike = isLikes.containsKey(comment.getId())
                    && isLikes.get(comment.getId()) == CommentLikeStatus.ACTIVE;
                // likeCounts 에서 commetId 가 있다면 반환 없으면 0
                long likeCount = likeCounts.getOrDefault(comment.getId(), 0L);
                return CommentReadResponse.from(
                    comment,
                    comment.getUserEntity().getId().equals(userId), // isEditable
                    isLike, // 좋아요 여부
                    likeCount // 좋아요 수
                );
            }
        ).toList();

        return CommentReadPaginationResponse.from(user.getProfileImgUrl(), comments, commentPage);

    }
    private void validateReview(Long reviewId) {
        reviewJpaRepository.findByIdAndStatus(reviewId, ReviewStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.COMMENT_READ_ALL_PAGINATION_INVALID,
                    HttpStatus.NOT_FOUND)).toModel();
    }

    private User getUserById(Long userId) {
        return userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND))
            .toModel();
    }
}
