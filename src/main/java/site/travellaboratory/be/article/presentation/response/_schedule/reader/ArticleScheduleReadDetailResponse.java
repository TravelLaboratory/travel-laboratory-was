package site.travellaboratory.be.article.presentation.response._schedule.reader;

import java.util.List;
import java.util.stream.Collectors;
import site.travellaboratory.be.article.infrastructure.persistence.entity._schedule.ArticleScheduleEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity._schedule.ScheduleEtcEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity._schedule.ScheduleGeneralEntity;
import site.travellaboratory.be.article.infrastructure.persistence.entity._schedule.ScheduleTransportEntity;

public record ArticleScheduleReadDetailResponse(
    Long reviewId,
    boolean isEditable,
    List<ArticleScheduleResponse> schedules
) {
    public static ArticleScheduleReadDetailResponse from(Long reviewId, boolean isEditable, List<ArticleScheduleEntity> schedules) {
        return new ArticleScheduleReadDetailResponse(
            reviewId,
            isEditable,
            schedules.stream().map(schedule -> {

                if (schedule instanceof ScheduleGeneralEntity general) {
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
                } else if (schedule instanceof ScheduleTransportEntity transport) {
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
                } else if (schedule instanceof ScheduleEtcEntity etc) {
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
