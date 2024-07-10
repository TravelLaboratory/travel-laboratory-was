package site.travellaboratory.be.presentation.articleschedule.dto.get;

import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.infrastructure.articleschedule.ArticleSchedule;
import site.travellaboratory.be.infrastructure.articleschedule.dtype.ScheduleEtc;
import site.travellaboratory.be.infrastructure.articleschedule.dtype.ScheduleGeneral;
import site.travellaboratory.be.infrastructure.articleschedule.dtype.ScheduleTransport;

public record ArticleScheduleReadDetailResponse(
    Long reviewId,
    boolean isEditable,
    List<ArticleScheduleResponse> schedules
) {
    public static ArticleScheduleReadDetailResponse from(Long reviewId, boolean isEditable, List<ArticleSchedule> schedules) {
        return new ArticleScheduleReadDetailResponse(
            reviewId,
            isEditable,
            schedules.stream().map(schedule -> {

                if (schedule instanceof ScheduleGeneral general) {
                    return new ArticleScheduleResponse(
                        schedule.getId(),
                        schedule.getVisitedDate(),
                        schedule.getVisitedTime(),
                        schedule.getSortOrder(),
                        schedule.getCategory(),
                        schedule.getDurationTime(),
                        schedule.getExpense(),
                        schedule.getMemo(),
                        schedule.getDtype(),
                        new ScheduleGeneralResponse(
                            general.getPlaceName(),
                            general.getGoogleMapPlaceId(),
                            general.getGoogleMapLatitude(),
                            general.getGoogleMapLongitude(),
                            general.getGoogleMapAddress(),
                            general.getGoogleMapPhoneNumber(),
                            general.getGoogleMapHomePageUrl()
                        ),
                        null,
                        null
                    );
                } else if (schedule instanceof ScheduleTransport transport) {
                    return new ArticleScheduleResponse(
                        schedule.getId(),
                        schedule.getVisitedDate(),
                        schedule.getVisitedTime(),
                        schedule.getSortOrder(),
                        schedule.getCategory(),
                        schedule.getDurationTime(),
                        schedule.getExpense(),
                        schedule.getMemo(),
                        schedule.getDtype(),
                        null,
                        new ScheduleTransportResponse(
                            transport.getTransportation(),
                            transport.getStartPlaceName(),
                            transport.getGoogleMapStartPlaceAddress(),
                            transport.getGoogleMapStartLatitude(),
                            transport.getGoogleMapStartLongitude(),
                            transport.getEndPlaceName(),
                            transport.getGoogleMapEndPlaceAddress(),
                            transport.getGoogleMapEndLatitude(),
                            transport.getGoogleMapEndLongitude()
                        ),
                        null
                    );
                } else if (schedule instanceof ScheduleEtc etc) {
                    return new ArticleScheduleResponse(
                        schedule.getId(),
                        schedule.getVisitedDate(),
                        schedule.getVisitedTime(),
                        schedule.getSortOrder(),
                        schedule.getCategory(),
                        schedule.getDurationTime(),
                        schedule.getExpense(),
                        schedule.getMemo(),
                        schedule.getDtype(),
                        null,
                        null,
                        new ScheduleEtcResponse(
                            etc.getPlaceName()
                        ));
                }
                return null;
            }).collect(Collectors.toList())
        );
    }
}
