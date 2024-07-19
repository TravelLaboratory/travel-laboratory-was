package site.travellaboratory.be.article.domain._schedule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Time;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.article.domain.Article;
import site.travellaboratory.be.article.domain._schedule.enums.ArticleScheduleStatus;
import site.travellaboratory.be.article.domain._schedule.request.ArticleScheduleRequest;
import site.travellaboratory.be.article.domain._schedule.request.ScheduleEtcRequest;
import site.travellaboratory.be.article.domain._schedule.request.ScheduleGeneralRequest;
import site.travellaboratory.be.article.domain._schedule.request.ScheduleTransportRequest;
import site.travellaboratory.be.article.domain.enums.ArticleStatus;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.user.domain.User;
import site.travellaboratory.be.user.domain.enums.UserStatus;

class ArticleScheduleTest {

    private User writer1;
    private Article article1;

    @BeforeEach
    void setUp() {
        // 유저
        writer1 = User.builder()
            .id(1L)
            .nickname("writer1")
            .status(UserStatus.ACTIVE)
            .build();

        // 유저가 쓴 여행계획
        article1 = Article.builder()
            .id(1L)
            .user(writer1)
            .status(ArticleStatus.ACTIVE)
            .build();
    }

    @Nested
    class createSchedule {

        @DisplayName("여행일정에_DTYPE이_3가지_중_하나가_아닌_경우_예외_반환")
        @Test
        void test1() {
            //given
            ArticleScheduleRequest saveRequest = ArticleScheduleRequest.builder()
                .dtype("ANYTHING")
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                ArticleSchedule.create(writer1, article1, saveRequest));
        }

        @DisplayName("ScheduleGeneral_본인이_작성한_여행계획이_아닌_경우_예외_반환")
        @Test
        void test2() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("otherUser")
                .status(UserStatus.ACTIVE)
                .build();

            ArticleScheduleRequest saveRequest = ArticleScheduleRequest.builder()
                .dtype("GENERAL")
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                ArticleSchedule.create(otherUser, article1, saveRequest));
        }

        @DisplayName("ScheduleTransport_본인이_작성한_여행계획이_아닌_경우_예외_반환")
        @Test
        void test3() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("otherUser")
                .status(UserStatus.ACTIVE)
                .build();

            ArticleScheduleRequest saveRequest = ArticleScheduleRequest.builder()
                .dtype("TRANSPORT")
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                ArticleSchedule.create(otherUser, article1, saveRequest));
        }

        @DisplayName("ScheduleEtc_본인이_작성한_여행계획이_아닌_경우_예외_반환")
        @Test
        void test4() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("otherUser")
                .status(UserStatus.ACTIVE)
                .build();

            ArticleScheduleRequest saveRequest = ArticleScheduleRequest.builder()
                .dtype("ETC")
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                ArticleSchedule.create(otherUser, article1, saveRequest));
        }

        @DisplayName("성공 - ScheduleGeneral_객체_생성")
        @Test
        void test1000() {
            //given
            LocalDate visitedDate = LocalDate.now();

            ArticleScheduleRequest scheduleSaveRequest = ArticleScheduleRequest.builder()
                .visitedDate(visitedDate)
                .visitedTime(Time.valueOf("10:00:00"))
                .sortOrder(1)
                .category("액티비티")
                .durationTime(Time.valueOf("02:00:00"))
                .expense("1000원")
                .memo("memo")
                .dtype("GENERAL")
                .scheduleGeneral(ScheduleGeneralRequest.builder()
                    .placeName("placeName")
                    .googleMapPlaceId("googleMapPlaceId")
                    .googleMapLatitude(37.5665)
                    .googleMapLongitude(126.9780)
                    .googleMapAddress("googleMapAddress")
                    .googleMapPhoneNumber("googleMapPhoneNumber")
                    .googleMapHomePageUrl("googleMapHomePageUrl")
                    .build())
                .scheduleTransport(null)
                .scheduleEtc(null)
                .build();

            //when
            ArticleSchedule articleSchedule = ArticleSchedule.create(writer1, article1,
                scheduleSaveRequest);

            //then
            assertNotNull(articleSchedule);
            assertEquals(visitedDate, articleSchedule.getVisitedDate());
            assertEquals(scheduleSaveRequest.visitedTime(), articleSchedule.getVisitedTime());
            assertEquals(scheduleSaveRequest.sortOrder(), articleSchedule.getSortOrder());
            assertEquals(scheduleSaveRequest.category(), articleSchedule.getCategory());
            assertEquals(scheduleSaveRequest.durationTime(), articleSchedule.getDurationTime());
            assertEquals(scheduleSaveRequest.expense(), articleSchedule.getExpense());
            assertEquals(scheduleSaveRequest.memo(), articleSchedule.getMemo());
            assertEquals(ArticleScheduleStatus.ACTIVE, articleSchedule.getStatus());
            assertEquals(scheduleSaveRequest.dtype(), articleSchedule.getDtype());

            ScheduleGeneral scheduleGeneral = (ScheduleGeneral) articleSchedule;
            assertEquals(scheduleSaveRequest.scheduleGeneral().placeName(), scheduleGeneral.getPlaceName());
            assertEquals(scheduleSaveRequest.scheduleGeneral().googleMapPlaceId(), scheduleGeneral.getGoogleMapPlaceId());
            assertEquals(scheduleSaveRequest.scheduleGeneral().googleMapLatitude(), scheduleGeneral.getGoogleMapLatitude());
            assertEquals(scheduleSaveRequest.scheduleGeneral().googleMapLongitude(), scheduleGeneral.getGoogleMapLongitude());
            assertEquals(scheduleSaveRequest.scheduleGeneral().googleMapAddress(), scheduleGeneral.getGoogleMapAddress());
            assertEquals(scheduleSaveRequest.scheduleGeneral().googleMapPhoneNumber(), scheduleGeneral.getGoogleMapPhoneNumber());
            assertEquals(scheduleSaveRequest.scheduleGeneral().googleMapHomePageUrl(), scheduleGeneral.getGoogleMapHomePageUrl());
        }

        @DisplayName("성공 - ScheduleTransport_객체_생성")
        @Test
        void test1001() {
            //given
            LocalDate visitedDate = LocalDate.now();

            ArticleScheduleRequest scheduleSaveRequest = ArticleScheduleRequest.builder()
                .visitedDate(visitedDate)
                .visitedTime(Time.valueOf("10:00:00"))
                .sortOrder(1)
                .category("이동")
                .durationTime(Time.valueOf("02:00:00"))
                .expense("1000원")
                .memo("memo")
                .dtype("TRANSPORT")
                .scheduleGeneral(null)
                .scheduleTransport(ScheduleTransportRequest.builder()
                    .transportation("Bus")
                    .startPlaceName("Start Place")
                    .googleMapStartPlaceAddress("Start Address")
                    .googleMapStartLatitude(37.5665)
                    .googleMapStartLongitude(126.9780)
                    .endPlaceName("End Place")
                    .googleMapEndPlaceAddress("End Address")
                    .googleMapEndLatitude(37.5651)
                    .googleMapEndLongitude(126.9784)
                    .build())
                .scheduleEtc(null)
                .build();

            //when
            ArticleSchedule articleSchedule = ArticleSchedule.create(writer1, article1,
                scheduleSaveRequest);

            //then
            assertNotNull(articleSchedule);
            assertEquals(visitedDate, articleSchedule.getVisitedDate());
            assertEquals(scheduleSaveRequest.visitedTime(), articleSchedule.getVisitedTime());
            assertEquals(scheduleSaveRequest.sortOrder(), articleSchedule.getSortOrder());
            assertEquals(scheduleSaveRequest.category(), articleSchedule.getCategory());
            assertEquals(scheduleSaveRequest.durationTime(), articleSchedule.getDurationTime());
            assertEquals(scheduleSaveRequest.expense(), articleSchedule.getExpense());
            assertEquals(scheduleSaveRequest.memo(), articleSchedule.getMemo());
            assertEquals(ArticleScheduleStatus.ACTIVE, articleSchedule.getStatus());
            assertEquals(scheduleSaveRequest.dtype(), articleSchedule.getDtype());

            ScheduleTransport scheduleTransport = (ScheduleTransport) articleSchedule;
            assertEquals(scheduleSaveRequest.scheduleTransport().transportation(), scheduleTransport.getTransportation());
            assertEquals(scheduleSaveRequest.scheduleTransport().startPlaceName(), scheduleTransport.getStartPlaceName());
            assertEquals(scheduleSaveRequest.scheduleTransport().googleMapStartPlaceAddress(), scheduleTransport.getGoogleMapStartPlaceAddress());
            assertEquals(scheduleSaveRequest.scheduleTransport().googleMapStartLatitude(), scheduleTransport.getGoogleMapStartLatitude());
            assertEquals(scheduleSaveRequest.scheduleTransport().googleMapStartLongitude(), scheduleTransport.getGoogleMapStartLongitude());
            assertEquals(scheduleSaveRequest.scheduleTransport().endPlaceName(), scheduleTransport.getEndPlaceName());
            assertEquals(scheduleSaveRequest.scheduleTransport().googleMapEndPlaceAddress(), scheduleTransport.getGoogleMapEndPlaceAddress());
            assertEquals(scheduleSaveRequest.scheduleTransport().googleMapEndLatitude(), scheduleTransport.getGoogleMapEndLatitude());
            assertEquals(scheduleSaveRequest.scheduleTransport().googleMapEndLongitude(), scheduleTransport.getGoogleMapEndLongitude());
        }

        @DisplayName("성공 - ScheduleEtc_객체_생성")
        @Test
        void test1002() {
            //given
            LocalDate visitedDate = LocalDate.now();

            ArticleScheduleRequest scheduleSaveRequest = ArticleScheduleRequest.builder()
                .visitedDate(visitedDate)
                .visitedTime(Time.valueOf("10:00:00"))
                .sortOrder(1)
                .category("기타")
                .durationTime(Time.valueOf("02:00:00"))
                .expense("1000원")
                .memo("memo")
                .dtype("ETC")
                .scheduleGeneral(null)
                .scheduleTransport(null)
                .scheduleEtc(ScheduleEtcRequest.builder()
                    .placeName("Etc Place")
                    .build())
                .build();

            //when
            ArticleSchedule articleSchedule = ArticleSchedule.create(writer1, article1,
                scheduleSaveRequest);

            //then
            assertNotNull(articleSchedule);
            assertEquals(visitedDate, articleSchedule.getVisitedDate());
            assertEquals(scheduleSaveRequest.visitedTime(), articleSchedule.getVisitedTime());
            assertEquals(scheduleSaveRequest.sortOrder(), articleSchedule.getSortOrder());
            assertEquals(scheduleSaveRequest.category(), articleSchedule.getCategory());
            assertEquals(scheduleSaveRequest.durationTime(), articleSchedule.getDurationTime());
            assertEquals(scheduleSaveRequest.expense(), articleSchedule.getExpense());
            assertEquals(scheduleSaveRequest.memo(), articleSchedule.getMemo());
            assertEquals(ArticleScheduleStatus.ACTIVE, articleSchedule.getStatus());
            assertEquals(scheduleSaveRequest.dtype(), articleSchedule.getDtype());

            ScheduleEtc scheduleEtc = (ScheduleEtc) articleSchedule;
            assertEquals(scheduleSaveRequest.scheduleEtc().placeName(), scheduleEtc.getPlaceName());
        }
    }

    @Nested
    class updateSchedule {

    }

    @Nested
    class deleteSchedule {
        private final Long existingScheduleId = 1L;

        @DisplayName("ScheduleGeneral- 본인이_작성하지_않은_여행계획의_일정을_삭제하려는_경우_예외_반환")
        @Test
        void test1() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("otherUser")
                .status(UserStatus.ACTIVE)
                .build();

            ScheduleGeneral scheduleGeneral = ScheduleGeneral.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("GENERAL")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                scheduleGeneral.delete(otherUser));
        }

        @DisplayName("ScheduleTransport- 본인이_작성하지_않은_여행계획의_일정을_삭제하려는_경우_예외_반환")
        @Test
        void test2() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("otherUser")
                .status(UserStatus.ACTIVE)
                .build();

            ScheduleTransport scheduleTransport = ScheduleTransport.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("TRANSPORT")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                scheduleTransport.delete(otherUser));
        }

        @DisplayName("ScheduleEtc- 본인이_작성하지_않은_여행계획의_일정을_삭제하려는_경우_예외_반환")
        @Test
        void test3() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("otherUser")
                .status(UserStatus.ACTIVE)
                .build();

            ScheduleEtc scheduleEtc = ScheduleEtc.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("ETC")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                scheduleEtc.delete(otherUser));
        }

        @DisplayName("성공 - GENERAL_Transport_ETC_3가지_일정_삭제_완료")
        @Test
        void test1000() {
            //given
            ScheduleGeneral scheduleGeneral = ScheduleGeneral.builder()
                .id(1L)
                .article(article1)
                .dtype("GENERAL")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();


            ScheduleTransport scheduleTransport = ScheduleTransport.builder()
                .id(2L)
                .article(article1)
                .dtype("TRANSPORT")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            ScheduleEtc scheduleEtc = ScheduleEtc.builder()
                .id(3L)
                .article(article1)
                .dtype("ETC")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            //when
            ScheduleGeneral deleteScheduleGeneral = scheduleGeneral.delete(writer1);
            ScheduleTransport deleteScheduleTransport = scheduleTransport.delete(writer1);
            ScheduleEtc deleteScheduleEtc = scheduleEtc.delete(writer1);

            //then
            assertEquals(ArticleScheduleStatus.INACTIVE, deleteScheduleGeneral.getStatus());
            assertEquals(ArticleScheduleStatus.INACTIVE, deleteScheduleTransport.getStatus());
            assertEquals(ArticleScheduleStatus.INACTIVE, deleteScheduleEtc.getStatus());
        }
    }
}