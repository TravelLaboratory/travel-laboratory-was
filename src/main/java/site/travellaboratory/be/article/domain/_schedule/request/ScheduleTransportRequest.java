package site.travellaboratory.be.article.domain._schedule.request;

import lombok.Builder;

public record ScheduleTransportRequest(
    String transportation,
    String startPlaceName,
    String googleMapStartPlaceAddress,
    Double googleMapStartLatitude,
    Double googleMapStartLongitude,
    String endPlaceName,
    String googleMapEndPlaceAddress,
    Double googleMapEndLatitude,
    Double googleMapEndLongitude
) {
    @Builder
    public ScheduleTransportRequest {
    }
}
