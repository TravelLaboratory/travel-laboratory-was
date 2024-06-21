package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.Location;
import site.travellaboratory.be.domain.article.TravelStyle;

public record ArticleSearchResponse(
        String title,
        List<Location> location,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String travelCompanion,
        List<String> travelStyles,
        String nickname,
        int totalPage,
        Long totalElements
) {

    public static Page<ArticleSearchResponse> from(final Page<Article> articles) {
        return articles.map(article -> new ArticleSearchResponse(
                article.getTitle(),
                article.getLocation(),
                article.getStartAt(),
                article.getEndAt(),
                article.getExpense(),
                article.getTravelCompanion().getName(),
                article.getTravelStyles().stream()
                        .map(TravelStyle::getName)
                        .collect(Collectors.toList()),
                article.getNickname(),
                articles.getTotalPages(),
                articles.getTotalElements()
        ));
    }

}
