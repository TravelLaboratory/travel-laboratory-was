package site.travellaboratory.be.presentation.articleschedule.dto.get;

public record ScheduleGeneralResponse(
    String placeName,
    String googleMapPlaceId,
    Double googleMapLatitude,
    Double googleMapLongitude,
    String googleMapAddress,
    String googleMapPhoneNumber,
    String googleMapHomePageUrl
) {

}
