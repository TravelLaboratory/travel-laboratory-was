package site.travellaboratory.be.service;

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
import site.travellaboratory.be.controller.comment.dto.CommentDeleteResponse;
import site.travellaboratory.be.controller.comment.dto.CommentLikeCount;
import site.travellaboratory.be.controller.comment.dto.CommentReadPaginationResponse;
import site.travellaboratory.be.controller.comment.dto.CommentReadResponse;
import site.travellaboratory.be.controller.comment.dto.CommentSaveRequest;
import site.travellaboratory.be.controller.comment.dto.CommentSaveResponse;
import site.travellaboratory.be.controller.comment.dto.userlikecomment.CommentToggleLikeResponse;
import site.travellaboratory.be.controller.comment.dto.CommentUpdateRequest;
import site.travellaboratory.be.controller.comment.dto.CommentUpdateResponse;
import site.travellaboratory.be.domain.comment.Comment;
import site.travellaboratory.be.domain.comment.CommentRepository;
import site.travellaboratory.be.domain.comment.CommentStatus;
import site.travellaboratory.be.domain.review.Review;
import site.travellaboratory.be.domain.review.ReviewRepository;
import site.travellaboratory.be.domain.review.ReviewStatus;
import site.travellaboratory.be.domain.user.UserRepository;
import site.travellaboratory.be.domain.user.entity.User;
import site.travellaboratory.be.domain.user.entity.UserStatus;
import site.travellaboratory.be.domain.userlikecomment.UserLikeComment;
import site.travellaboratory.be.domain.userlikecomment.UserLikeCommentRepository;
import site.travellaboratory.be.domain.userlikecomment.UserLikeCommentStatus;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final UserLikeCommentRepository userLikeCommentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CommentReadPaginationResponse readAllCommentPagination(
        Long userId, Long reviewId, int page, int size
    ) {
        // 유효하지 않은 후기를 조회할 경우
        // todo: (1) review.getUser() 이 때 쿼리 날라감 (FETCH JOIN) - 55 review user fetch join
        Review review = reviewRepository.findByIdAndStatusIn(reviewId,
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.COMMENT_READ_ALL_PAGINATION_INVALID,
                    HttpStatus.NOT_FOUND));

        // 나만보기 상태의 후기를 다른 유저가 조회할 경우
        if (review.getStatus() == ReviewStatus.PRIVATE && (!review.getUser().getId()
            .equals(userId))) {
            throw new BeApplicationException(ErrorCodes.COMMENT_READ_ALL_PAGINATION_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 후기 리스트 조회 (N+1 쿼리 해결)
        // todo: 62 ~ 82 단순 테이블안에서 변환 N+1 발생 X
        // (1) 페이징 처리
        Page<Comment> commentPage = commentRepository.findByReviewIdAndStatusOrderByCreatedAtDesc(reviewId,
            CommentStatus.ACTIVE, PageRequest.of(page, size));

        // (2) 좋아요 체크, 좋아요 수 알기 위해
        List<Long> commentIds = commentPage.getContent().stream()
            .map(Comment::getId) // comment table id로 변환
            .toList();

        // (3)-1 좋아요 수
        List<CommentLikeCount> commentLikeCounts = userLikeCommentRepository.countByCommentIdsAndStatusGroupByCommentId(
            commentIds,
            UserLikeCommentStatus.ACTIVE).stream().map(result -> new CommentLikeCount(
            (Long) result[0], (Long) result[1])).toList();

        // (3)-2 좋아요 수 개수 Map<commentId, likeCount> 변환
        Map<Long, Long> likeCounts = commentLikeCounts.stream()
            .collect(Collectors.toMap(
                CommentLikeCount::commentId,
                CommentLikeCount::likeCount
            ));

        // (4) userId와 commentId 들를 기준으로 데이터 리스트 뽑아오기 (N+1 해결)
        // todo: before after 비교해보기
        List<UserLikeComment> userLikeComments = userLikeCommentRepository.findAllByUserIdAndCommentIdInAndStatusFetchJoinComment(
            userId, commentIds,
            UserLikeCommentStatus.ACTIVE);
        // (4)-2 좋아요 체크 Map<commentId, UserLikeCommentStatus> 변환
        Map<Long, UserLikeCommentStatus> isLikes = userLikeComments.stream()
            .collect(Collectors.toMap(
                userLikeComment -> userLikeComment.getComment().getId(),
                UserLikeComment::getStatus
            ));

        // todo : test - FETCH JOIN으로 N+1 해결하기 (먼저 commentId에 관련된 User 가져오기)
        // (4) 댓글과 연관된 유저 정보를 포함한 응답 생성
        // commentPage.getContent() 에서 commentIds 를 이용해 user 를 fetch join 으로 로딩
        // todo : after N+1 해결
        List<Comment> commentsWithUsers = commentRepository.findAllByIdInFetchJoinUser(commentIds, CommentStatus.ACTIVE);

        // (5) 최종 데이터 반환
        // todo : before N+1 문제
//        List<CommentReadResponse> comments = commentPage.stream().map(comment -> {
            // todo : after N+1 해결
        List<CommentReadResponse> comments = commentsWithUsers.stream().map(comment -> {
                // isLikes 에서 commentId 가 있고, 그게 ACTIVE 라면 true
                boolean isLike = isLikes.containsKey(comment.getId())
                    && isLikes.get(comment.getId()) == UserLikeCommentStatus.ACTIVE;
                // likeCounts 에서 commetId 가 있다면 반환 없으면 0
                long likeCount = likeCounts.getOrDefault(comment.getId(), 0L);
                return CommentReadResponse.from(
                    comment,
                    comment.getUser().getId().equals(userId), // isEditable
                    isLike, // 좋아요 여부
                    likeCount // 좋아요 수
                );
            }
        ).toList();

        // (댓글을 쓰는 유저 조회) todo: refactoring
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        return CommentReadPaginationResponse.from(user.getProfileImgUrl(), comments, commentPage);
    }

    @Transactional
    public CommentSaveResponse saveComment(Long userId, CommentSaveRequest request) {
        // 유효하지 않은 후기에 대한 댓글을 작성할 경우
        Review review = reviewRepository.findByIdAndStatusIn(request.reviewId(),
                List.of(ReviewStatus.ACTIVE, ReviewStatus.PRIVATE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_POST_INVALID,
                HttpStatus.NOT_FOUND));

        // 댓글 쓰는 유저 찾기
        User commentUser = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(
                () -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        // 댓글 작성
        Comment saveComment = commentRepository.save(
            Comment.of(
                commentUser,
                review,
                request.replyComment()
            )
        );
        return CommentSaveResponse.from(saveComment.getId());
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long userId, Long commentId,
        CommentUpdateRequest request) {
        // 유효하지 않은 댓글를 수정할 경우
        Comment comment = commentRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_UPDATE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 댓글이 아닌 경우
        if (!comment.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.COMMENT_UPDATE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 댓글 업데이트
        comment.update(request.replyComment());
        Comment updateComment = commentRepository.save(comment);
        return CommentUpdateResponse.from(updateComment.getId());
    }

    @Transactional
    public CommentDeleteResponse deleteComment(final Long userId, final Long commentId) {
        // 유효하지 않은 댓글을 삭제할 경우
        Comment comment = commentRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_DELETE_INVALID,
                HttpStatus.NOT_FOUND));

        // 유저가 작성한 댓글이 아닌 경우
        if (!comment.getUser().getId().equals(userId)) {
            throw new BeApplicationException(ErrorCodes.COMMENT_DELETE_NOT_USER,
                HttpStatus.FORBIDDEN);
        }

        // 댓글 삭제
        comment.delete();
        commentRepository.save(comment);
        return CommentDeleteResponse.from(true);
    }

    @Transactional
    public CommentToggleLikeResponse toggleLikeComment(Long userId, Long commentId) {
        // 유효하지 않은 댓글에 좋아요하려고 할 경우
        Comment comment = commentRepository.findByIdAndStatusIn(commentId,
                List.of(CommentStatus.ACTIVE))
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.COMMENT_LIKE_INVALID,
                HttpStatus.NOT_FOUND));

        UserLikeComment userLikeComment = userLikeCommentRepository.findByUserIdAndCommentId(userId,
                commentId)
            .orElse(null);

        // 댓글에 처음 좋아요를 누른 게 아닌 경우
        if (userLikeComment != null) {
            userLikeComment.toggleStatus();
        } else {
            // 댓글에 처음 좋아요를 누른 경우 - 새로 생성
            User user = User.of(userId);
            userLikeComment = UserLikeComment.of(user, comment);
        }

        UserLikeComment saveLikeComment = userLikeCommentRepository.save(userLikeComment);
        return CommentToggleLikeResponse.from(saveLikeComment.getStatus());
    }
}
