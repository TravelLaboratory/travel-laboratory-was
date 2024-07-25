package site.travellaboratory.be.article.infrastructure.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.domain.enums.TravelCompanion;
import site.travellaboratory.be.article.domain.enums.TravelStyle;
import site.travellaboratory.be.article.domain.request.ArticleRegisterRequest;
import site.travellaboratory.be.article.domain.request.LocationRequest;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class ArticleEntityTest {

    private User writer;
    private LocationRequest location;
    private LocalDate startAt;
    private LocalDate endAt;
    private Article article;

    @BeforeEach
    void setUp() {
        writer = User.builder()
                .id(1L)
                .nickname("테스트 유저 1")
                .profileImgUrl(null)
                .introduce("소개 1")
                .status(UserStatus.ACTIVE)
                .build();

        location = new LocationRequest("123.1234", "xx구 yy동", "서울");
        startAt = LocalDate.now();
        endAt = LocalDate.now().plusDays(5);

        final ArticleRegisterRequest register =
                ArticleRegisterRequest.builder()
                        .title("title")
                        .locations(List.of(location))
                        .startAt(startAt)
                        .endAt(endAt)
                        .expense("10000원")
                        .travelCompanion(TravelCompanion.ALONE.getName())
                        .travelStyles(
                                List.of(TravelStyle.ACTIVITY.getName(), TravelStyle.HOCANCE.getName()))
                        .build();

        article = Article.create(writer, register);
    }

    @DisplayName("toModel 메서드가 올바르게 Article 객체를 반환하는지 테스트")
    @Test
    void toModel_test() {
        // given
        final ArticleEntity articleEntity = ArticleEntity.from(article);

        // when
        final Article article = articleEntity.toModel();

        // then
        assertThat(article.getId()).isEqualTo(articleEntity.getId());
        assertThat(article.getUser().getId()).isEqualTo(writer.getId());
        assertThat(article.getTitle()).isEqualTo(articleEntity.getTitle());
        assertThat(article.getLocations().size()).isEqualTo(articleEntity.getLocationEntities().size());
        assertThat(article.getStartAt()).isEqualTo(articleEntity.getStartAt());
        assertThat(article.getEndAt()).isEqualTo(articleEntity.getEndAt());
        assertThat(article.getExpense()).isEqualTo(articleEntity.getExpense());
        assertThat(article.getTravelCompanion()).isEqualTo(articleEntity.getTravelCompanion());
        assertThat(article.getTravelStyles()).containsExactlyElementsOf(articleEntity.getTravelStyles());
        assertThat(article.getStatus()).isEqualTo(articleEntity.getStatus());
        assertThat(article.getCoverImgUrl()).isEqualTo(articleEntity.getCoverImgUrl());
    }

    @DisplayName("from 메소드는 ArticleEntity객체를 생성해야 한다.")
    @Test
    void from_test() {
        //given

        //when
        final ArticleEntity articleEntity = ArticleEntity.from(article);

        //then
        assertThat(articleEntity).isInstanceOf(ArticleEntity.class);
    }

    @DisplayName("updateCoverImage 메소드는 coverImgUrl을 변경해야 한다.")
    @Test
    void updateCoverImage_test() {
        //given
        final String newCoverImgUrl = "새로운 url";

        final ArticleEntity articleEntity = ArticleEntity.from(article);

        //when
        articleEntity.updateCoverImage(newCoverImgUrl);

        //then
        assertThat(articleEntity.getCoverImgUrl()).isEqualTo("새로운 url");
    }

    @DisplayName("delete 메소드는 ArticleStatus 상태를 INACTIVE로 변경해야 한다.")
    @Test
    void delete_test() {
        //given
        final ArticleEntity articleEntity = ArticleEntity.from(article);

        //when
        articleEntity.delete();

        //then
        assertThat(articleEntity.getStatus()).isEqualTo(ArticleStatus.INACTIVE);
    }

    @DisplayName("togglePrivacyStatus 메소드는 ACTIVE면 PRIVATE로, PRIVATE면 ACTIVE로 변경해야 한다.")
    @Test
    void togglePrivacyStatus_test() {
        //given
        final ArticleEntity articleEntity = ArticleEntity.from(article);

        //when
        articleEntity.togglePrivacyStatus();

        //then
        assertThat(articleEntity.getStatus()).isEqualTo(ArticleStatus.PRIVATE);
        articleEntity.togglePrivacyStatus();
        assertThat(articleEntity.getStatus()).isEqualTo(ArticleStatus.ACTIVE);
    }

    @DisplayName("updateStatus 메소드는 ArticleStatus 상태를 변경해야 한다.")
    @Test
    void updateStatus_test() {
        //given
        final ArticleEntity articleEntity = ArticleEntity.from(article);

        //when
        articleEntity.updateStatus(ArticleStatus.INACTIVE);

        //then
        assertThat(articleEntity.getStatus()).isEqualTo(ArticleStatus.INACTIVE);
    }
}
