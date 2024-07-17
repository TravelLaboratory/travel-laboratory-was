package site.travellaboratory.be.article.presentation.response._schedule.reader;

// todo : jpa entity 이름 수정 후엔 response 제거
public record ScheduleTransportResponse(
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

}
