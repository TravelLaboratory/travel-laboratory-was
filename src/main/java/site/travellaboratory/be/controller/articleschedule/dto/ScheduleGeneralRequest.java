package site.travellaboratory.be.controller.articleschedule.dto;

import java.sql.Time;

public record ScheduleGeneralRequest(
    String placeName,
    Time durationTime,
    String expense,
    String memo,
    Long googleMapPlaceId,
    Double googleMapLatitude,
    Double googleMapLongitude,
    String googleMapAddress,
    String googleMapPhoneNumber,
    String googleMapHomePageUrl
) {

}
