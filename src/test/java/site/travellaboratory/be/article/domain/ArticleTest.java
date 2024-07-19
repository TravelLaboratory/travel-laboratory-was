package site.travellaboratory.be.article.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.article.domain.enums.TravelCompanion;
import site.travellaboratory.be.article.domain.enums.TravelStyle;
import site.travellaboratory.be.article.domain.request.ArticleRegisterRequest;
import site.travellaboratory.be.article.domain.request.ArticleUpdateRequest;
import site.travellaboratory.be.article.domain.request.LocationRequest;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

public class ArticleTest {

    private User writer;

    @BeforeEach
    void setUp() {
        writer = User.builder()
            .id(1L)
            .nickname("writer")
            .profileImgUrl(null)
            .introduce("userA-introduce")
            .status(UserStatus.ACTIVE)
            .build();
    }

    @Nested
    class createArticle {

        private LocationRequest location;
        private LocalDate startAt;
        private LocalDate endAt;

        @BeforeEach
        void setUp() {
            location = new LocationRequest("123.1234", "xx구 yy동", "서울");
            startAt = LocalDate.now();
            endAt = LocalDate.now().plusDays(5);
        }

        @DisplayName("유효하지_않은_travelCompanion_값을_입력한_경우_예외_반환")
        @Test
        void test1() {
            //given
            String invalidTravelCompanion = "유효하지 않은 travelComapanion";

            ArticleRegisterRequest invalidRegister =
                ArticleRegisterRequest.builder()
                    .title("title")
                    .locations(List.of(location))
                    .startAt(startAt)
                    .endAt(endAt)
                    .expense("10000원")
                    .travelCompanion(invalidTravelCompanion)
                    .travelStyles(
                        List.of(TravelStyle.ACTIVITY.getName(), TravelStyle.HOCANCE.getName()))
                    .build();

            //when, then
            assertThrows(BeApplicationException.class, () -> {
                TravelCompanion.from(invalidRegister.travelCompanion());
            });

            assertThrows(BeApplicationException.class, () -> {
                Article.create(writer, invalidRegister);
            });
        }

        @DisplayName("유효하지_않은_travelStyles_값을_입력한_경우_예외_반환")
        @Test
        void test2() {
            //given
            String invalidTravelStyle = "유효하지 않은 travelStyle";

            ArticleRegisterRequest invalidRegister =
                ArticleRegisterRequest.builder()
                    .title("title")
                    .locations(List.of(location))
                    .startAt(startAt)
                    .endAt(endAt)
                    .expense("10000원")
                    .travelCompanion(TravelCompanion.ALONE.getName())
                    .travelStyles(List.of(invalidTravelStyle))
                    .build();

            //when, then
            assertThrows(BeApplicationException.class, () -> {
                TravelStyle.from(invalidRegister.travelStyles());
            });

            assertThrows(BeApplicationException.class, () -> {
                Article.create(writer, invalidRegister);
            });
        }

        @DisplayName("성공 - ArticleRegisterRequest_으로_Article_객체_생성")
        @Test
        void test1000() {
            //given

            ArticleRegisterRequest register =
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

            //when
            Article article = Article.create(writer, register);

            //then
            assertNotNull(article);
            assertEquals(register.title(), article.getTitle());
            assertEquals(register.locations().size(), article.getLocations().size());
            assertEquals(register.startAt(), article.getStartAt());
            assertEquals(register.endAt(), article.getEndAt());
            assertEquals(register.endAt(), article.getEndAt());
            assertEquals(register.expense(), article.getExpense());
            assertEquals(TravelCompanion.from(register.travelCompanion()),
                article.getTravelCompanion());
            assertEquals(TravelStyle.from(register.travelStyles()), article.getTravelStyles());
        }
    }

    @Nested
    class UpdateArticle {

        private Article article;

        @BeforeEach
        void setUp() {
            article = Article.create(writer,
                ArticleRegisterRequest.builder()
                    .title("title")
                    .locations(List.of(
                        LocationRequest.builder().placeId("123.1234").address("xx구 yy동").city("서울")
                            .build()))
                    .startAt(LocalDate.now())
                    .endAt(LocalDate.now().plusDays(5))
                    .expense("10000원")
                    .travelCompanion(TravelCompanion.ALONE.getName())
                    .travelStyles(
                        List.of(TravelStyle.ACTIVITY.getName(), TravelStyle.HOCANCE.getName()))
                    .build());
        }

        @DisplayName("유효하지_않은_travelCompanion_값을_입력한_경우_예외_반환")
        @Test
        void test1() {
            //given
            String invalidCompanion = "유효하지 않은 travelCompanion";

            ArticleUpdateRequest invalidUpdate = ArticleUpdateRequest.builder()
                .title("updateTitle")
                .locations(List.of(
                    LocationRequest.builder().placeId("456.5678").address("aa구 bb동").city("부산")
                        .build()))
                .startAt(LocalDate.now().plusDays(10))
                .endAt(LocalDate.now().plusDays(15))
                .expense("10000원")
                .travelCompanion(invalidCompanion)
                .travelStyles(List.of(TravelStyle.HEALING.getName()))
                .build();

            //when, then
            assertThrows(BeApplicationException.class, () -> {
                TravelCompanion.from(invalidUpdate.travelCompanion());
            });

            assertThrows(BeApplicationException.class, () -> {
                article.update(writer, invalidUpdate);
            });
        }

        @DisplayName("유효하지_않은_travelStyles_값을_입력한_경우_예외_반환")
        @Test
        void test2() {
            //given
            String invalidTravelStyle = "유효하지 않은 travelStyle";

            ArticleUpdateRequest invalidUpdate = ArticleUpdateRequest.builder()
                .title("updateTitle")
                .locations(List.of(
                    LocationRequest.builder().placeId("456.5678").address("aa구 bb동").city("부산")
                        .build()))
                .startAt(LocalDate.now().plusDays(10))
                .endAt(LocalDate.now().plusDays(15))
                .expense("10000원")
                .travelCompanion(TravelCompanion.PARENTS.getName())
                .travelStyles(List.of(invalidTravelStyle))
                .build();

            //when, then
            assertThrows(BeApplicationException.class, () -> {
                TravelStyle.from(invalidUpdate.travelStyles());
            });

            assertThrows(BeApplicationException.class, () -> {
                article.update(writer, invalidUpdate);
            });
        }

        @DisplayName("작성자가_아닌_다른_유저가_수정하려고하는_경우_예외_반환")
        @Test
        void test3() {
            // given
            User notWriter = User.builder()
                .id(2L)
                .nickname("notWriter")
                .profileImgUrl(null)
                .introduce("introduce")
                .status(UserStatus.ACTIVE)
                .build();

            ArticleUpdateRequest articleUpdateRequest = ArticleUpdateRequest.builder()
                .title("updateTitle")
                .locations(List.of(
                    LocationRequest.builder().placeId("456.5678").address("aa구 bb동").city("부산")
                        .build()))
                .startAt(LocalDate.now().plusDays(10))
                .endAt(LocalDate.now().plusDays(15))
                .expense("10000원")
                .travelCompanion(TravelCompanion.PARENTS.getName())
                .travelStyles(
                    List.of(TravelStyle.HOT_PLACE.getName(), TravelStyle.WITH_NATURE.getName(),
                        TravelStyle.CAFE_TOUR.getName()))
                .build();

            // when & then
            assertThrows(BeApplicationException.class, () -> {
                article.update(notWriter, articleUpdateRequest);
            });
        }

        @DisplayName("성공 - ArticleUpdateRequest_으로_Article_객체_수정")
        @Test
        void test1000() {
            //given
            LocationRequest updateLocation = new LocationRequest("456.5678", "aa구 bb동", "부산");
            LocalDate updateStartAt = LocalDate.now().plusDays(10);
            LocalDate updateEndAt = LocalDate.now().plusDays(15);

            ArticleUpdateRequest update = new ArticleUpdateRequest(
                "updateTitle", List.of(updateLocation), updateStartAt, updateEndAt, "15000원",
                TravelCompanion.ACTOR.getName(),
                List.of(TravelStyle.HOT_PLACE.getName(), TravelStyle.WITH_NATURE.getName(),
                    TravelStyle.CAFE_TOUR.getName()));

            //when
            Article updateArticle = article.update(writer, update);

            //then
            assertNotNull(updateArticle);
            assertEquals(update.title(), updateArticle.getTitle());
            assertEquals(update.locations().size(), updateArticle.getLocations().size());
            assertEquals(update.startAt(), updateArticle.getStartAt());
            assertEquals(update.endAt(), updateArticle.getEndAt());
            assertEquals(update.endAt(), updateArticle.getEndAt());
            assertEquals(update.expense(), updateArticle.getExpense());
            assertEquals(TravelCompanion.from(update.travelCompanion()),
                updateArticle.getTravelCompanion());
            assertEquals(TravelStyle.from(update.travelStyles()), updateArticle.getTravelStyles());
        }
    }

    @Nested
    class deleteArticle {

        private Article article;

        @BeforeEach
        void setUp() {
            article = Article.create(writer,
                ArticleRegisterRequest.builder()
                    .title("title")
                    .locations(List.of(
                        LocationRequest.builder().placeId("123.1234").address("xx구 yy동").city("서울")
                            .build()))
                    .startAt(LocalDate.now())
                    .endAt(LocalDate.now().plusDays(5))
                    .expense("10000원")
                    .travelCompanion(TravelCompanion.ALONE.getName())
                    .travelStyles(
                        List.of(TravelStyle.ACTIVITY.getName(), TravelStyle.HOCANCE.getName()))
                    .build());
        }

        @DisplayName("글쓴이가_아닌_다른_유저가_삭제하려고_하는_경우_예외_반환")
        @Test
        void test() {
            //given
            User notWriter = User.builder()
                .id(2L)
                .nickname("notWriter")
                .profileImgUrl(null)
                .introduce("introduce")
                .status(UserStatus.ACTIVE)
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () -> {
                article.delete(notWriter);
            });
        }

        @DisplayName("성공 - 글쓴이가_삭제하려고_하는_경우")
        @Test
        void test1000() {
            //when
            Article deleteArticle = article.delete(writer);

            //then
            assertEquals(ArticleStatus.INACTIVE, deleteArticle.getStatus());
        }
    }
}