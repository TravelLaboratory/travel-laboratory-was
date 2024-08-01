package site.travellaboratory.be.common.presentation.response;

import java.util.Map;

public record ApiErrorResponse(
    String localMessage,
    Long code,
    Map<String, String> fieldErrors
) {
}