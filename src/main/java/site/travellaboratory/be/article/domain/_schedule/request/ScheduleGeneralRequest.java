package site.travellaboratory.be.article.domain._schedule.request;

import lombok.Builder;

public record ScheduleGeneralRequest(
    String placeName,
    String googleMapPlaceId,
    Double googleMapLatitude,
    Double googleMapLongitude,
    String googleMapAddress,
    String googleMapPhoneNumber,
    String googleMapHomePageUrl
) {
    @Builder
    public ScheduleGeneralRequest {
    }
}
