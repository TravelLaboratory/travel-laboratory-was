package site.travellaboratory.be.controller.article.dto;

import jakarta.validation.constraints.NotNull;

public record ArticleSearchRequest (
        @NotNull
        String keyWord
){
}
