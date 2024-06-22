package site.travellaboratory.be.controller.articleschedule.dto.put;

public record ScheduleGeneralRequest(
    String placeName,
    String googleMapPlaceId,
    Double googleMapLatitude,
    Double googleMapLongitude,
    String googleMapAddress,
    String googleMapPhoneNumber,
    String googleMapHomePageUrl
) {

}
