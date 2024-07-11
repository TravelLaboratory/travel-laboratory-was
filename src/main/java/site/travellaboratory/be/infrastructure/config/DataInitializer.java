package site.travellaboratory.be.infrastructure.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import site.travellaboratory.be.infrastructure.domains.auth.pwquestion.entity.PwQuestion;
import site.travellaboratory.be.infrastructure.domains.auth.pwquestion.PwQuestionRepository;

@Profile("!test") // 혹시 몰라서 Profile test 는 실행되지 않게끔 설정
@Configuration
public class DataInitializer {

    @Bean
    @ConditionalOnProperty(name = "spring.jpa.hibernate.ddl-auto", havingValue = "create")
    CommandLineRunner initDatabasePwQuestion(PwQuestionRepository pwQuestionRepository) {
        return args -> {
            List<PwQuestion> questions = Arrays.asList(
                new PwQuestion("가장 기억에 남는 반려동물의 이름은 무엇인가요?"),
                new PwQuestion("유년 시절 꿈꾸던 직업은 무엇인가요?"),
                new PwQuestion("가장 좋아하는 인물은 누구인가요?"),
                new PwQuestion("가장 좋아하는 소설의 제목은 무엇인가요?"),
                new PwQuestion("첫 번째로 간 해외 여행지는 어디인가요?"),
                new PwQuestion("가장 즐겨하는 스포츠는 무엇인가요?"),
                new PwQuestion("유년 시절의 별명은 무엇인가요?"),
                new PwQuestion("다니던 초등학교의 이름은 무엇인가요?")
            );
            pwQuestionRepository.saveAll(questions);
        };
    }
}
