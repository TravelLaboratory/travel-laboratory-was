package site.travellaboratory.be.user.domain._pw;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import site.travellaboratory.be.user.domain._pw.enums.PwAnswerStatus;

class PwAnswerTest {
    @Nested
    class CreatePwAnswerTest {

        @DisplayName("성공 - PwAnswer 객체 생성")
        @Test
        void test1000() {
            //given
            Long userId = 1L;
            Long pwQuestionId = 1L;
            String answer = "myAnswer";

            //when
            PwAnswer pwAnswer = PwAnswer.create(userId, pwQuestionId, answer);

            //then
            assertNotNull(pwAnswer);
            assertEquals(userId, pwAnswer.getUserId());
            assertEquals(pwQuestionId, pwAnswer.getPwQuestionId());
            assertEquals(answer, pwAnswer.getAnswer());
            assertEquals(PwAnswerStatus.ACTIVE, pwAnswer.getStatus());
        }
    }
}