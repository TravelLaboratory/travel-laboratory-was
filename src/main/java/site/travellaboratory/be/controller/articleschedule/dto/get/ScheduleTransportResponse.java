package site.travellaboratory.be.controller.articleschedule.dto.get;

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
