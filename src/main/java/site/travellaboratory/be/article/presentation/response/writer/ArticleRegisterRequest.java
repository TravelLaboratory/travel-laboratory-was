package site.travellaboratory.be.article.presentation.response.writer;

import java.time.LocalDate;
import java.util.List;

public record ArticleRegisterRequest(
    String title,
    List<LocationDto> locations,
    LocalDate startAt,
    LocalDate endAt,
    String expense,
    String travelCompanion,
    List<String> travelStyles
) {

}
