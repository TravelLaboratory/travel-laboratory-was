package site.travellaboratory.be.controller.article.dto;

import jakarta.validation.constraints.NotBlank;

public record ArticleSearchRequest (
        @NotBlank
        String keyWord
){
}
