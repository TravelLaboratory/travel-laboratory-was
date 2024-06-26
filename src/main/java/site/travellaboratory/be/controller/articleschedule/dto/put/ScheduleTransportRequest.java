package site.travellaboratory.be.controller.articleschedule.dto.put;

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

}
