package site.travellaboratory.be.article.domain._schedule.request;

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
