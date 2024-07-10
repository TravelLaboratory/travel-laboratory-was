package site.travellaboratory.be.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.travellaboratory.be.common.exception.BeApplicationException;
import site.travellaboratory.be.common.exception.ErrorCodes;
import site.travellaboratory.be.presentation.user.dto.UserDeleteResponse;
import site.travellaboratory.be.presentation.user.dto.UserProfileResponse;
import site.travellaboratory.be.presentation.user.dto.UserProfileUpdateRequest;
import site.travellaboratory.be.presentation.user.dto.UserProfileUpdateResponse;
import site.travellaboratory.be.infrastructure.article.entity.Article;
import site.travellaboratory.be.infrastructure.article.ArticleRepository;
import site.travellaboratory.be.infrastructure.article.enums.ArticleStatus;
import site.travellaboratory.be.infrastructure.bookmark.entity.Bookmark;
import site.travellaboratory.be.infrastructure.bookmark.BookmarkRepository;
import site.travellaboratory.be.infrastructure.bookmark.enums.BookmarkStatus;
import site.travellaboratory.be.infrastructure.user.UserRepository;
import site.travellaboratory.be.infrastructure.user.entity.User;
import site.travellaboratory.be.infrastructure.user.enums.UserStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final BookmarkRepository bookmarkRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;

    public UserProfileResponse findByUserProfile(final Long userId, final Long id) {
        final User user = userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.AUTH_USER_NOT_FOUND,
                        HttpStatus.BAD_REQUEST)
                );

        final boolean isEditable = user.getId().equals(userId);

        return UserProfileResponse.from(user, isEditable);
    }

    public UserProfileUpdateResponse updateProfile(
            final MultipartFile file,
            final UserProfileUpdateRequest userProfileUpdateRequest,
            final Long userId
    ) {
        final User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND));

        String url = user.getProfileImgUrl();

        if (file != null && !file.isEmpty()) {
            url = uploadFiles(file);
        }

        user.update(userProfileUpdateRequest.nickname(),
                url, userProfileUpdateRequest.introduce());

        return new UserProfileUpdateResponse(user.getNickname(),
                user.getProfileImgUrl(),
                user.getIntroduce());
    }

    private String uploadFiles(final MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
            return fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Transactional
    public UserDeleteResponse deleteUser(final Long userId) {
        User user = userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<Article> articles = articleRepository.findByUserAndStatusIn(user,
                        List.of(ArticleStatus.ACTIVE, ArticleStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.ARTICLE_NOT_FOUND, HttpStatus.NOT_FOUND));

        articles.forEach(article -> article.updateStatus(ArticleStatus.INACTIVE));
        articleRepository.saveAll(articles);

        List<Bookmark> bookmarks = bookmarkRepository.findByUserAndStatusIn(user,
                        List.of(BookmarkStatus.ACTIVE, BookmarkStatus.PRIVATE))
                .orElseThrow(() -> new BeApplicationException(ErrorCodes.BOOKMARK_NOT_FOUND, HttpStatus.NOT_FOUND));

        bookmarks.forEach(bookmark -> bookmark.updateStatus(BookmarkStatus.INACTIVE));
        bookmarkRepository.saveAll(bookmarks);

        // 사용자 삭제 처리
        user.delete();
        userRepository.save(user);

        return UserDeleteResponse.from(true);
    }

}
