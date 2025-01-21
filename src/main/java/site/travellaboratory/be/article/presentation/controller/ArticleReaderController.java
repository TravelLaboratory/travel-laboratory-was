package site.travellaboratory.be.article.presentation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.travellaboratory.be.article.application.service.ArticleReaderService;
import site.travellaboratory.be.article.presentation.response.like.BookmarkResponse;
import site.travellaboratory.be.article.presentation.response.reader.ArticleOneResponse;
import site.travellaboratory.be.article.presentation.response.reader.ArticleTotalResponse;
import site.travellaboratory.be.article.presentation.response.reader.BannerArticlesResponse;
import site.travellaboratory.be.common.annotation.UserId;
import site.travellaboratory.be.common.presentation.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ArticleReaderController {

    private final ArticleReaderService articleReaderService;

    @GetMapping("/users/{userId}/articles")
    public ApiResponse<Page<ArticleTotalResponse>> findArticlesByUser(
            @UserId final Long loginId,
            @PathVariable(name = "userId") final Long userId,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size
    ) {
        final Page<ArticleTotalResponse> articleResponse = articleReaderService.findByUserArticles(loginId, userId,
                PageRequest.of(page, size));
        return ApiResponse.OK(articleResponse);
    }

    @GetMapping("/articles")
    public ApiResponse<Page<ArticleTotalResponse>> findArticles(
            @UserId final Long loginId,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,DESC") String sort
    ) {
        final Page<ArticleTotalResponse> articleTotalResponses = articleReaderService.searchAllArticles(loginId,
                PageRequest.of(page, size), sort);
        return ApiResponse.OK(articleTotalResponses);
    }

    //
    @GetMapping("/articles/{articleId}")
    public ApiResponse<ArticleOneResponse> findArticle(
            @UserId final Long loginId,
            @PathVariable(name = "articleId") final Long articleId
    ) {
        final ArticleOneResponse articleResponse = articleReaderService.findByArticle(loginId, articleId);
        return ApiResponse.OK(articleResponse);
    }


    @GetMapping("/articles/search")
    public ApiResponse<Page<ArticleTotalResponse>> searchArticle(
            @RequestParam("keyword") final String keyword,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size,
            @RequestParam(required = false, defaultValue = "createdAt,desc") String sort,
            @UserId final Long loginId
    ) {
        final Page<ArticleTotalResponse> response = articleReaderService.searchArticlesByKeyWord(keyword,
                PageRequest.of(page, size), loginId, sort);
        return ApiResponse.OK(response);
    }

    @GetMapping("/bookmarks/{userId}")
    public ApiResponse<Page<BookmarkResponse>> findMyAllBookmark(
            @UserId final Long loginId,
            @PathVariable(name = "userId") final Long userId,
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size
    ) {
        final Page<BookmarkResponse> allBookmarkByUser = articleReaderService.findAllBookmarkByUser(loginId, userId,
                PageRequest.of(page, size));

        return ApiResponse.OK(allBookmarkByUser);
    }

    /*
    * 인기 - 이번 주 좋아요를 많이 누른 여행 계획 (feat. 30일 이내, 좋아요 수 기준)
    * */
    @GetMapping("/banner/articles/likes")
    public ApiResponse<List<BannerArticlesResponse>> readBannerArticlesLikes() {
        List<BannerArticlesResponse> articles = articleReaderService.readBannerArticlesByWeeklyLikes();
        return ApiResponse.OK(articles);
    }

    /*
    * 트렌딩 - 최근 핫한 여행 계획 (feat. 30일 이내, 조회 수 기준)
    * */
    @GetMapping("/banner/articles/hot")
    public ApiResponse<List<BannerArticlesResponse>> readBannerArticlesHot() {
        List<BannerArticlesResponse> articles = articleReaderService.readBannerArticlesByHourlyViews();
        return ApiResponse.OK(articles);
    }
}
