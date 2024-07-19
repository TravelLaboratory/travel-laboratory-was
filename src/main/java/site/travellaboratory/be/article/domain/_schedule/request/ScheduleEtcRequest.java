package site.travellaboratory.be.article.domain._schedule.request;

import lombok.Builder;

public record ScheduleEtcRequest(
    String placeName
) {
    @Builder
    public ScheduleEtcRequest {
    }
}
