package site.travellaboratory.be.article.presentation.response._schedule.reader;

// todo : jpa entity 이름 수정 후엔 response 제거
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
