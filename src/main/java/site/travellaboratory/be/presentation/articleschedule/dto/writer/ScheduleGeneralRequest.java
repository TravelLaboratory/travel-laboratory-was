package site.travellaboratory.be.presentation.articleschedule.dto.writer;

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
