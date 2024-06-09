package site.travellaboratory.be.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.travellaboratory.be.controller.dto.ArticleRegisterRequest;
import site.travellaboratory.be.controller.dto.ArticleResponse;
import site.travellaboratory.be.domain.article.Article;
import site.travellaboratory.be.domain.article.ArticleRepository;
import site.travellaboratory.be.domain.user.User;
import site.travellaboratory.be.domain.user.UserRepository;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public Long saveArticle(final Long userId, final ArticleRegisterRequest articleRegisterRequest) {
        final User user = userRepository.getById(userId);
        final Article article = Article.of(user, articleRegisterRequest);
        articleRepository.save(article);
        return article.getId();
    }

    public List<ArticleResponse> findByUserArticles(final Long userId) {
        final User user = userRepository.getById(userId);
        final List<Article> articles = articleRepository.findArticlesByUser(user);
        return ArticleResponse.from(articles);
    }

    public ArticleResponse findByUserArticle(final Long articleId) {
        final Article article = articleRepository.getById(articleId);
        return ArticleResponse.from(article);
    }
}

