package site.travellaboratory.be.controller.article.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.Location;
import site.travellaboratory.be.domain.article.TravelStyle;

public record ArticleSearchResponse(
        String title,
        List<Location> location,
        LocalDate startAt,
        LocalDate endAt,
        String expense,
        String imageUrl,
        String travelCompanion,
        List<String> travelStyles,
        String nickname
) {

    public static Page<ArticleSearchResponse> from(final Page<Article> articles) {
        return articles.map(article -> new ArticleSearchResponse(
                article.getTitle(),
                article.getLocation(),
                article.getStartAt(),
                article.getEndAt(),
                article.getExpense(),
                article.getUser().getProfileImgUrl(),
                article.getTravelCompanion().getName(),
                article.getTravelStyles().stream()
                        .map(TravelStyle::getName)
                        .collect(Collectors.toList()),
                article.getNickname()
        ));
    }

}
