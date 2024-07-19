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
        private final Long existingScheduleId = 1L;

        @DisplayName("ScheduleGeneral- 본인이_작성하지_않은_여행계획의_일정을_수정하려는_경우_예외_반환")
        @Test
        void test1() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("otherUser")
                .status(UserStatus.ACTIVE)
                .build();

            ArticleSchedule articleSchedule = ScheduleGeneral.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("GENERAL")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            ArticleScheduleRequest scheduleUpdateRequest = ArticleScheduleRequest.builder()
                .scheduleId(existingScheduleId)
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                articleSchedule.update(otherUser, scheduleUpdateRequest));
        }

        @DisplayName("ScheduleTransport- 본인이_작성하지_않은_여행계획의_일정을_수정하려는_경우_예외_반환")
        @Test
        void test2() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("otherUser")
                .status(UserStatus.ACTIVE)
                .build();

            ArticleSchedule articleSchedule = ScheduleTransport.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("TRANSPORT")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            ArticleScheduleRequest scheduleUpdateRequest = ArticleScheduleRequest.builder()
                .scheduleId(existingScheduleId)
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                articleSchedule.update(otherUser, scheduleUpdateRequest));
        }

        @DisplayName("ScheduleEtc- 본인이_작성하지_않은_여행계획의_일정을_수정하려는_경우_예외_반환")
        @Test
        void test3() {
            //given
            User otherUser = User.builder()
                .id(2L)
                .nickname("otherUser")
                .status(UserStatus.ACTIVE)
                .build();

            ArticleSchedule articleSchedule = ScheduleEtc.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("ETC")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            ArticleScheduleRequest scheduleUpdateRequest = ArticleScheduleRequest.builder()
                .scheduleId(existingScheduleId)
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                articleSchedule.update(otherUser, scheduleUpdateRequest));
        }

        @DisplayName("ScheduleGeneral- schedule_id가_존재하지_않는_여행계획의_일정을_수정하려는_경우_예외_반환")
        @Test
        void test4() {
            //given
            ArticleSchedule articleSchedule = ScheduleGeneral.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("GENERAL")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            ArticleScheduleRequest scheduleUpdateRequest = ArticleScheduleRequest.builder()
                .dtype("GENERAL")
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                articleSchedule.update(writer1, scheduleUpdateRequest));
        }

        @DisplayName("ScheduleTransport- schedule_id가_존재하지_않는_여행계획의_일정을_수정하려는_경우_예외_반환")
        @Test
        void test5() {
            //given
            ArticleSchedule articleSchedule = ScheduleGeneral.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("TRANSPORT")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            ArticleScheduleRequest scheduleUpdateRequest = ArticleScheduleRequest.builder()
                .dtype("TRANSPORT")
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                articleSchedule.update(writer1, scheduleUpdateRequest));
        }

        @DisplayName("ScheduleEtc- schedule_id가_존재하지_않는_여행계획의_일정을_수정하려는_경우_예외_반환")
        @Test
        void test6() {
            //given
            ArticleSchedule articleSchedule = ScheduleGeneral.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("ETC")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            ArticleScheduleRequest scheduleUpdateRequest = ArticleScheduleRequest.builder()
                .dtype("ETC")
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                articleSchedule.update(writer1, scheduleUpdateRequest));
        }

        @DisplayName("성공 - GENERAL_타입_일정_수정_완료")
        @Test
        void test1000() {
            //given
            LocalDate visitedDate = LocalDate.now();
            ArticleSchedule articleSchedule = ScheduleGeneral.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("GENERAL")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            ArticleScheduleRequest scheduleUpdateGeneral = ArticleScheduleRequest.builder()
                .scheduleId(existingScheduleId)
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
            ArticleSchedule updateArticleSchedule = articleSchedule.update(writer1, scheduleUpdateGeneral);

            //then
            assertNotNull(updateArticleSchedule);
            assertEquals(visitedDate, updateArticleSchedule.getVisitedDate());
            assertEquals(scheduleUpdateGeneral.visitedTime(), updateArticleSchedule.getVisitedTime());
            assertEquals(scheduleUpdateGeneral.sortOrder(), updateArticleSchedule.getSortOrder());
            assertEquals(scheduleUpdateGeneral.category(), updateArticleSchedule.getCategory());
            assertEquals(scheduleUpdateGeneral.durationTime(), updateArticleSchedule.getDurationTime());
            assertEquals(scheduleUpdateGeneral.expense(), updateArticleSchedule.getExpense());
            assertEquals(scheduleUpdateGeneral.memo(), updateArticleSchedule.getMemo());
            assertEquals(ArticleScheduleStatus.ACTIVE, updateArticleSchedule.getStatus());
            assertEquals(scheduleUpdateGeneral.dtype(), updateArticleSchedule.getDtype());

            ScheduleGeneral updateScheduleGeneral = (ScheduleGeneral) updateArticleSchedule;
            assertEquals(scheduleUpdateGeneral.scheduleGeneral().placeName(), updateScheduleGeneral.getPlaceName());
            assertEquals(scheduleUpdateGeneral.scheduleGeneral().googleMapPlaceId(), updateScheduleGeneral.getGoogleMapPlaceId());
            assertEquals(scheduleUpdateGeneral.scheduleGeneral().googleMapLatitude(), updateScheduleGeneral.getGoogleMapLatitude());
            assertEquals(scheduleUpdateGeneral.scheduleGeneral().googleMapLongitude(), updateScheduleGeneral.getGoogleMapLongitude());
            assertEquals(scheduleUpdateGeneral.scheduleGeneral().googleMapAddress(), updateScheduleGeneral.getGoogleMapAddress());
            assertEquals(scheduleUpdateGeneral.scheduleGeneral().googleMapPhoneNumber(), updateScheduleGeneral.getGoogleMapPhoneNumber());
            assertEquals(scheduleUpdateGeneral.scheduleGeneral().googleMapHomePageUrl(), updateScheduleGeneral.getGoogleMapHomePageUrl());
        }

        @DisplayName("성공 - TRANSPORT_타입_일정_수정_완료")
        @Test
        void test1001() {
            //given
            LocalDate visitedDate = LocalDate.now();
            ArticleSchedule articleSchedule = ScheduleTransport.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("TRANSPORT")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();


            ArticleScheduleRequest scheduleUpdateTransport = ArticleScheduleRequest.builder()
                .scheduleId(existingScheduleId)
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
            ArticleSchedule updateArticleSchedule = articleSchedule.update(writer1, scheduleUpdateTransport);

            //then
            assertNotNull(updateArticleSchedule);
            assertEquals(visitedDate, updateArticleSchedule.getVisitedDate());
            assertEquals(scheduleUpdateTransport.visitedTime(), updateArticleSchedule.getVisitedTime());
            assertEquals(scheduleUpdateTransport.sortOrder(), updateArticleSchedule.getSortOrder());
            assertEquals(scheduleUpdateTransport.category(), updateArticleSchedule.getCategory());
            assertEquals(scheduleUpdateTransport.durationTime(), updateArticleSchedule.getDurationTime());
            assertEquals(scheduleUpdateTransport.expense(), updateArticleSchedule.getExpense());
            assertEquals(scheduleUpdateTransport.memo(), updateArticleSchedule.getMemo());
            assertEquals(ArticleScheduleStatus.ACTIVE, updateArticleSchedule.getStatus());
            assertEquals(scheduleUpdateTransport.dtype(), updateArticleSchedule.getDtype());

            ScheduleTransport updateScheduleTransport = (ScheduleTransport) updateArticleSchedule;
            assertEquals(scheduleUpdateTransport.scheduleTransport().transportation(), updateScheduleTransport.getTransportation());
            assertEquals(scheduleUpdateTransport.scheduleTransport().startPlaceName(), updateScheduleTransport.getStartPlaceName());
            assertEquals(scheduleUpdateTransport.scheduleTransport().googleMapStartPlaceAddress(), updateScheduleTransport.getGoogleMapStartPlaceAddress());
            assertEquals(scheduleUpdateTransport.scheduleTransport().googleMapStartLatitude(), updateScheduleTransport.getGoogleMapStartLatitude());
            assertEquals(scheduleUpdateTransport.scheduleTransport().googleMapStartLongitude(), updateScheduleTransport.getGoogleMapStartLongitude());
            assertEquals(scheduleUpdateTransport.scheduleTransport().endPlaceName(), updateScheduleTransport.getEndPlaceName());
            assertEquals(scheduleUpdateTransport.scheduleTransport().googleMapEndPlaceAddress(), updateScheduleTransport.getGoogleMapEndPlaceAddress());
            assertEquals(scheduleUpdateTransport.scheduleTransport().googleMapEndLatitude(), updateScheduleTransport.getGoogleMapEndLatitude());
            assertEquals(scheduleUpdateTransport.scheduleTransport().googleMapEndLongitude(), updateScheduleTransport.getGoogleMapEndLongitude());
        }

        @DisplayName("성공 - ETC_타입_일정_수정_완료")
        @Test
        void test1002() {
            //given
            LocalDate visitedDate = LocalDate.now();
            ArticleSchedule articleSchedule = ScheduleEtc.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("ETC")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();


            ArticleScheduleRequest scheduleUpdateEtc = ArticleScheduleRequest.builder()
                .scheduleId(existingScheduleId)
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
            ArticleSchedule updateArticleSchedule = articleSchedule.update(writer1, scheduleUpdateEtc);

            //then
            assertNotNull(updateArticleSchedule);
            assertEquals(visitedDate, updateArticleSchedule.getVisitedDate());
            assertEquals(scheduleUpdateEtc.visitedTime(), updateArticleSchedule.getVisitedTime());
            assertEquals(scheduleUpdateEtc.sortOrder(), updateArticleSchedule.getSortOrder());
            assertEquals(scheduleUpdateEtc.category(), updateArticleSchedule.getCategory());
            assertEquals(scheduleUpdateEtc.durationTime(), updateArticleSchedule.getDurationTime());
            assertEquals(scheduleUpdateEtc.expense(), updateArticleSchedule.getExpense());
            assertEquals(scheduleUpdateEtc.memo(), updateArticleSchedule.getMemo());
            assertEquals(ArticleScheduleStatus.ACTIVE, updateArticleSchedule.getStatus());
            assertEquals(scheduleUpdateEtc.dtype(), updateArticleSchedule.getDtype());

            ScheduleEtc updateScheduleEtc = (ScheduleEtc) updateArticleSchedule;
            assertEquals(scheduleUpdateEtc.scheduleEtc().placeName(), updateScheduleEtc.getPlaceName());
        }
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

            ArticleSchedule articleSchedule = ScheduleGeneral.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("GENERAL")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                articleSchedule.delete(otherUser));
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

            ArticleSchedule articleSchedule = ScheduleTransport.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("TRANSPORT")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                articleSchedule.delete(otherUser));
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

            ArticleSchedule articleSchedule  = ScheduleEtc.builder()
                .id(existingScheduleId)
                .article(article1)
                .dtype("ETC")
                .status(ArticleScheduleStatus.ACTIVE)
                .build();

            //when & then
            assertThrows(BeApplicationException.class, () ->
                articleSchedule.delete(otherUser));
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
            ArticleSchedule articleScheduleGeneral = scheduleGeneral.delete(writer1);
            ArticleSchedule articleScheduleTransport = scheduleTransport.delete(writer1);
            ArticleSchedule articleScheduleEtc = scheduleEtc.delete(writer1);

            //then
            assertEquals(ArticleScheduleStatus.INACTIVE, articleScheduleGeneral.getStatus());
            assertEquals(ArticleScheduleStatus.INACTIVE, articleScheduleTransport.getStatus());
            assertEquals(ArticleScheduleStatus.INACTIVE, articleScheduleEtc.getStatus());
        }
    }
}