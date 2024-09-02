package site.travellaboratory.be.user.application._auth;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.travellaboratory.be.common.presentation.exception.BeApplicationException;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;
import site.travellaboratory.be.article.infrastructure.persistence.repository.ArticleJpaRepository;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.infrastructure.persistence.repository.BookmarkRepository;
import site.travellaboratory.be.article.infrastructure.persistence.entity.BookmarkEntity;
import site.travellaboratory.be.article.domain.enums.BookmarkStatus;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.presentation._auth.response.userunregistration.UserUnregisterResponse;

@Service
@RequiredArgsConstructor
public class UserUnregistrationService {

    // todo: 회원이 작성했던 여행 계획, 리뷰 등등 다 비활성화처리필요함
    private final UserJpaRepository userJpaRepository;
    private final ArticleJpaRepository articleJpaRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public UserUnregisterResponse unregister(final Long userId) {
        UserEntity userEntity = userJpaRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<ArticleEntity> articleJpaEntities = articleJpaRepository.findByUserEntityAndStatus(
                userEntity, ArticleStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        articleJpaEntities.forEach(article -> article.updateStatus(ArticleStatus.INACTIVE));
        articleJpaRepository.saveAll(articleJpaEntities);

        List<BookmarkEntity> bookmarkEntities = bookmarkRepository.findByUserEntityAndStatus(userEntity, BookmarkStatus.ACTIVE)
            .orElseThrow(() -> new BeApplicationException(ErrorCodes.BOOKMARK_NOT_FOUND, HttpStatus.NOT_FOUND));

        bookmarkEntities.forEach(bookmark -> bookmark.updateStatus(BookmarkStatus.INACTIVE));
        bookmarkRepository.saveAll(bookmarkEntities);

        // 사용자 삭제 처리
        userEntity.delete();
        userJpaRepository.save(userEntity);

        return UserUnregisterResponse.from(true);
    }
}
