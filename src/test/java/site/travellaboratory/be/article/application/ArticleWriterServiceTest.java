package site.travellaboratory.be.article.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.article.application.service.ArticleWriterService;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.domain.enums.TravelCompanion;
import site.travellaboratory.be.article.domain.enums.TravelStyle;
import site.travellaboratory.be.article.domain.request.ArticleRegisterRequest;
import site.travellaboratory.be.article.domain.request.ArticleUpdateRequest;
import site.travellaboratory.be.article.domain.request.LocationRequest;
import site.travellaboratory.be.article.infrastructure.persistence.entity.ArticleEntity;
import site.travellaboratory.be.article.infrastructure.persistence.repository.ArticleJpaRepository;
import site.travellaboratory.be.article.presentation.response.writer.ArticleUpdateCoverImageResponse;
import site.travellaboratory.be.article.presentation.response.writer.ArticleUpdatePrivacyResponse;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.infrastructure.aws.S3FileUploader;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;
import site.travellaboratory.be.user.infrastructure.persistence.repository.UserJpaRepository;

@ExtendWith(MockitoExtension.class)
class ArticleWriterServiceTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private ArticleJpaRepository articleJpaRepository;

    @Mock
    private S3FileUploader s3FileUploader;

    @InjectMocks
    private ArticleWriterService articleWriterService;

    @Test
    @DisplayName("saveArticle 메소드는 존재하는 유저일 경우 article을 저장한다.")
    void saveArticleTest_exist_user() {
        // given
        final User user = user();

        final UserEntity userEntity = UserEntity.from(user);

        when(userJpaRepository.findByIdAndStatus(1L, UserStatus.ACTIVE))
                .thenReturn(Optional.of(userEntity));

        final ArticleRegisterRequest articleRegisterRequest = articleRegisterRequest();

        final Article article = Article.create(user, articleRegisterRequest);
        final ArticleEntity articleEntity = ArticleEntity.from(article);

        when(articleJpaRepository.save(any(ArticleEntity.class)))
                .thenReturn(articleEntity);

        // when
        final Long savedArticleId = articleWriterService.saveArticle(1L, articleRegisterRequest);

        // then
        assertThat(savedArticleId).isEqualTo(articleEntity.getId());
    }

    @DisplayName("saveArticle 메소드는 존재 하지 않는 유저 일 경우 예외가 발생한다.")
    @Test
    void saveArticleTest_not_exist_user() {
        //given
        when(userJpaRepository.findByIdAndStatus(1L, UserStatus.ACTIVE))
                .thenReturn(Optional.empty());

        final ArticleRegisterRequest articleRegisterRequest = articleRegisterRequest();

        //when & then
        assertThatThrownBy(() -> articleWriterService.saveArticle(1L, articleRegisterRequest)).isInstanceOf(
                BeApplicationException.class);
    }

    @DisplayName("커버 이미지 수정 성공 테스트")
    @Test
    void updateCoverImage_test_success() {
        //given
        MultipartFile coverImage = mock(MultipartFile.class);

        final String imageUrl = "testUrl";

        final User user = user();

        final ArticleRegisterRequest articleRegisterRequest = articleRegisterRequest();

        final Article article = Article.create(user, articleRegisterRequest);
        final ArticleEntity articleEntity = ArticleEntity.from(article);

        when(articleJpaRepository.findByIdAndStatusIn(any(Long.class), any(List.class)))
                .thenReturn(Optional.of(articleEntity));
        when(s3FileUploader.uploadFiles(coverImage))
                .thenReturn(imageUrl);

        //when
        final ArticleUpdateCoverImageResponse response = articleWriterService.updateCoverImage(coverImage, 1L);

        //then
        assertThat(response.coverImgUrl()).isEqualTo(imageUrl);
        assertThat(articleEntity.getCoverImgUrl()).isEqualTo(imageUrl);
    }

    @DisplayName("유효하지 않은 게시물에 커버 이미지를 바꾸면 예외가 발생한다.")
    @Test
    void updateCoverImage_test_ifArticleNotFound() {
        //given
        final MultipartFile coverImage = mock(MultipartFile.class);

        when(articleJpaRepository.findByIdAndStatusIn(any(Long.class), any(List.class)))
                .thenReturn(Optional.empty());

        ///when & then
        assertThatThrownBy(() -> articleWriterService.updateCoverImage(coverImage, 1L)).isInstanceOf(
                BeApplicationException.class);
    }

    @DisplayName("유효하지 않은 유저가 수정을 하면 예외가 발생한다.")
    @Test
    void updateArticle_test_ifUserIsNotFound() {
        //given
        when(userJpaRepository.findByIdAndStatus(any(Long.class), eq(UserStatus.ACTIVE)))
                .thenReturn(Optional.empty());

        final ArticleUpdateRequest articleUpdateRequest = articleUpdateRequest();

        //when & then
        assertThatThrownBy(() -> articleWriterService.updateArticle(1L, 1L, articleUpdateRequest)).isInstanceOf(
                BeApplicationException.class);
    }

    @DisplayName("유효하지 않은 게시물에 수정을 하면 예외가 발생한다.")
    @Test
    void updateArticle_test_ifArticleIsNotFound() {
        //given
        when(userJpaRepository.findByIdAndStatus(any(Long.class), eq(UserStatus.ACTIVE)))
                .thenReturn(Optional.of(UserEntity.from(user())));
        when(articleJpaRepository.findByIdAndStatusIn(any(Long.class), any(List.class)))
                .thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> articleWriterService.updateArticle(1L, 1L, articleUpdateRequest())).isInstanceOf(
                BeApplicationException.class);
    }

    @DisplayName("게시물 수정 성공 테스트")
    @Test
    void updateArticle_test_success() {
        //given
        final Article article = Article.create(user(), articleRegisterRequest());
        final ArticleEntity articleEntity = ArticleEntity.from(article);

        final Article newArticle = article.update(user(), articleUpdateRequest());

        when(userJpaRepository.findByIdAndStatus(any(Long.class), eq(UserStatus.ACTIVE)))
                .thenReturn(Optional.of(UserEntity.from(user())));
        when(articleJpaRepository.findByIdAndStatusIn(any(Long.class), any(List.class)))
                .thenReturn(Optional.of(articleEntity));
        when(articleJpaRepository.save(any(ArticleEntity.class)))
                .thenReturn(ArticleEntity.from(newArticle));

        //when
        final Article updateArticle = articleWriterService.updateArticle(1L, 1L, articleUpdateRequest());

        //then
        assertThat(updateArticle.getTitle()).isEqualTo(articleUpdateRequest().title());
        assertThat(updateArticle.getLocations().size()).isEqualTo(articleUpdateRequest().locations().size());
        assertThat(updateArticle.getStartAt()).isEqualTo(articleUpdateRequest().startAt());
        assertThat(updateArticle.getEndAt()).isEqualTo(articleUpdateRequest().endAt());
        assertThat(updateArticle.getExpense()).isEqualTo(articleUpdateRequest().expense());
        assertThat(updateArticle.getTravelCompanion().getName()).isEqualTo(articleUpdateRequest().travelCompanion());
        assertThat(updateArticle.getTravelStyles().stream()
                .map(TravelStyle::getName)).containsExactlyElementsOf(
                articleUpdateRequest().travelStyles());
    }

    @DisplayName("유효하지 않은 게시물의 접근 권한을 변경하려고 할 경우 예외가 발생한다.")
    @Test
    void updateArticlePrivacy_ifArticleIsNotFound() {
        //given
        when(articleJpaRepository.findByIdAndStatusIn(any(Long.class), any(List.class)))
                .thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> articleWriterService.updateArticlePrivacy(1L, 1L)).isInstanceOf(
                BeApplicationException.class);

    }

    @DisplayName("변경 권한이 없는 유저가 변경하려고 할 경우 예외가 발생한다.")
    @Test
    void updateArticlePrivacy_not_authority_user() {
        //given
        final Long articleId = 1L;
        final Long nonAuthorUserId = 2L;
        final Long tryingUserId = 1L;

        final ArticleEntity articleEntity = mock(ArticleEntity.class);
        final UserEntity userEntity = mock(UserEntity.class);

        when(articleJpaRepository.findByIdAndStatusIn(eq(articleId), any(List.class)))
                .thenReturn(Optional.of(articleEntity));
        when(articleEntity.getUserEntity()).thenReturn(userEntity);
        when(userEntity.getId()).thenReturn(nonAuthorUserId);

        //when & then
        assertThatThrownBy(() -> articleWriterService.updateArticlePrivacy(tryingUserId, articleId))
                .isInstanceOf(BeApplicationException.class);
    }

    @DisplayName("유저가 자신의 여행 계획의 공개 상태를 성공적으로 업데이트할 수 있다.")
    @Test
    void updateArticlePrivacy_success() {
        //given
        final Long userId = 1L;
        final Long articleId = 1L;

        final ArticleEntity articleEntity = mock(ArticleEntity.class);
        final UserEntity userEntity = mock(UserEntity.class);

        when(articleJpaRepository.findByIdAndStatusIn(eq(userId), any(List.class)))
                .thenReturn(Optional.of(articleEntity));
        when(articleEntity.getUserEntity()).thenReturn(userEntity);
        when(articleEntity.getUserEntity().getId()).thenReturn(userId);

        doAnswer(invocation -> {
            when(articleEntity.getStatus()).thenReturn(ArticleStatus.PRIVATE);
            return null;
        }).when(articleEntity).togglePrivacyStatus();

        //when
        ArticleUpdatePrivacyResponse response = articleWriterService.updateArticlePrivacy(userId, articleId);

        //then
        verify(articleEntity).togglePrivacyStatus();
        assertThat(response.isPrivate()).isTrue();
    }

    private User user() {
        return User.builder()
                .id(1L)
                .nickname("테스트 유저 1")
                .profileImgUrl(null)
                .introduce("소개 1")
                .status(UserStatus.ACTIVE)
                .build();
    }

    private ArticleRegisterRequest articleRegisterRequest() {
        final LocationRequest location = new LocationRequest("123.1234", "xx구 yy동", "서울");
        final LocalDate startAt = LocalDate.now();
        final LocalDate endAt = LocalDate.now().plusDays(5);

        return ArticleRegisterRequest.builder()
                .title("title")
                .locations(List.of(location))
                .startAt(startAt)
                .endAt(endAt)
                .expense("10000원")
                .travelCompanion(TravelCompanion.ALONE.getName())
                .travelStyles(
                        List.of(TravelStyle.ACTIVITY.getName(), TravelStyle.HOCANCE.getName()))
                .build();
    }

    private ArticleUpdateRequest articleUpdateRequest() {
        final LocationRequest location = new LocationRequest("123.1234", "xx구 yy동", "서울");
        final LocalDate startAt = LocalDate.now();
        final LocalDate endAt = LocalDate.now().plusDays(5);

        return ArticleUpdateRequest.builder()
                .title("title1")
                .locations(List.of(location))
                .startAt(startAt)
                .endAt(endAt)
                .expense("100001원")
                .travelCompanion(TravelCompanion.ALONE.getName())
                .travelStyles(
                        List.of(TravelStyle.ACTIVITY.getName(), TravelStyle.HOCANCE.getName()))
                .build();
    }
}
