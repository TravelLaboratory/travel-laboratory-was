package site.travellaboratory.be.application.comment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.infrastructure.domains.comment.repository.CommentJpaRepository;
import site.travellaboratory.be.infrastructure.domains.comment.entity.CommentJpaEntity;
import site.travellaboratory.be.domain.comment.enums.CommentStatus;
import site.travellaboratory.be.infrastructure.domains.review.repository.ReviewJpaRepository;
import site.travellaboratory.be.infrastructure.domains.review.entity.ReviewJpaEntity;
import site.travellaboratory.be.domain.review.enums.ReviewStatus;
import site.travellaboratory.be.infrastructure.domains.user.UserJpaRepository;
import site.travellaboratory.be.infrastructure.domains.user.entity.UserJpaEntity;
import site.travellaboratory.be.domain.user.enums.UserStatus;
import site.travellaboratory.be.infrastructure.domains.comment.repository.CommentLikeJpaRepository;
import site.travellaboratory.be.infrastructure.domains.comment.entity.CommentLikeJpaEntity;
import site.travellaboratory.be.domain.comment.enums.CommentLikeStatus;
import site.travellaboratory.be.presentation.comment.dto.reader.CommentLikeCount;
import site.travellaboratory.be.presentation.comment.dto.reader.CommentReadPaginationResponse;
import site.travellaboratory.be.presentation.comment.dto.reader.CommentReadResponse;

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
        // todo: (1) review.getUser() 이 때 쿼리 날라감 (FETCH JOIN) - 55 review user fetch join
        ReviewJpaEntity reviewJpaEntity = reviewJpaRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.COMMENT_READ_ALL_PAGINATION_INVALID,
                    HttpStatus.NOT_FOUND));

        // 나만보기 상태의 후기를 다른 유저가 조회할 경우
        if (reviewJpaEntity.getStatus() == ReviewStatus.PRIVATE && (!reviewJpaEntity.getUserJpaEntity().getId()
            .equals(userId))) {
            throw new BeApplicationException(ErrorCodes.COMMENT_READ_ALL_PAGINATION_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 후기 리스트 조회 (N+1 쿼리 해결)
        // todo: 62 ~ 82 단순 테이블안에서 변환 N+1 발생 X
        // (1) 페이징 처리
        Page<CommentJpaEntity> commentPage = commentJpaRepository.findByReviewJpaEntityIdAndStatusOrderByCreatedAtDesc(reviewId,
            CommentStatus.ACTIVE, PageRequest.of(page, size));

        // (2) 좋아요 체크, 좋아요 수 알기 위해
        List<Long> commentIds = commentPage.getContent().stream()
            .map(CommentJpaEntity::getId) // comment table id로 변환
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
        // todo: before after 비교해보기
        List<CommentLikeJpaEntity> commentLikeJpaEntities = commentLikeJpaRepository.findAllByUserIdAndCommentIdInAndStatusFetchJoinComment(
            userId, commentIds,
            CommentLikeStatus.ACTIVE);
        // (4)-2 좋아요 체크 Map<commentId, UserLikeCommentStatus> 변환
        Map<Long, CommentLikeStatus> isLikes = commentLikeJpaEntities.stream()
            .collect(Collectors.toMap(
                commentLikeJpaEntity -> commentLikeJpaEntity.getCommentJpaEntity().getId(),
                CommentLikeJpaEntity::getStatus
            ));

        // todo : test - FETCH JOIN으로 N+1 해결하기 (먼저 commentId에 관련된 User 가져오기)
        // (4) 댓글과 연관된 유저 정보를 포함한 응답 생성
        // commentPage.getContent() 에서 commentIds 를 이용해 user 를 fetch join 으로 로딩
        // todo : after N+1 해결
        List<CommentJpaEntity> commentsWithUsers = commentJpaRepository.findAllByIdInFetchJoinUser(commentIds, CommentStatus.ACTIVE);

        // (5) 최종 데이터 반환
        // todo : before N+1 문제
//        List<CommentReadResponse> comments = commentPage.stream().map(comment -> {
            // todo : after N+1 해결
        List<CommentReadResponse> comments = commentsWithUsers.stream().map(comment -> {
                // isLikes 에서 commentId 가 있고, 그게 ACTIVE 라면 true
                boolean isLike = isLikes.containsKey(comment.getId())
                    && isLikes.get(comment.getId()) == CommentLikeStatus.ACTIVE;
                // likeCounts 에서 commetId 가 있다면 반환 없으면 0
                long likeCount = likeCounts.getOrDefault(comment.getId(), 0L);
                return CommentReadResponse.from(
                    comment,
                    comment.getUserJpaEntity().getId().equals(userId), // isEditable
                    isLike, // 좋아요 여부
                    likeCount // 좋아요 수
                );
            }
        ).toList();

        // (댓글을 쓰는 유저 조회) todo: refactoring
        UserJpaEntity userJpaEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        return CommentReadPaginationResponse.from(userJpaEntity.getProfileImgUrl(), comments, commentPage);
    }
}
