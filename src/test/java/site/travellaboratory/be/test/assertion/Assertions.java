package site.travellaboratory.be.test.assertion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;
import org.springframework.test.web.servlet.MvcResult;
import site.travellaboratory.be.common.presentation.error.ErrorCodes;
import site.travellaboratory.be.user.domain.enums.UserStatus;
import site.travellaboratory.be.user.infrastructure.persistence.entity.UserEntity;

public class Assertions {

    // todo: 수정 예정
    private static final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());


    public static void assertMvcErrorEquals(
        MvcResult result, ErrorCodes errorCodes
    ) throws UnsupportedEncodingException, JsonProcessingException {
        final String content = result.getResponse().getContentAsString();
        final JsonNode responseBody = objectMapper.readTree(content);
        final JsonNode resultField = responseBody.get("error");
        final JsonNode dataField = responseBody.get("data");


        assertNotNull(resultField);
        assertTrue(resultField.isObject());
        assertEquals(errorCodes.code, resultField.get("code").asLong());
        assertEquals(errorCodes.message, resultField.get("local_message").asText());
        assertTrue(resultField.get("field_errors").isNull());
        assertTrue(dataField.isNull());
    }

    public static void assertMvcDataEquals(
        MvcResult result, Consumer<JsonNode> consumer
    ) throws UnsupportedEncodingException, JsonProcessingException {
        final String content = result.getResponse().getContentAsString();
        final JsonNode responseBody = objectMapper.readTree(content);
        JsonNode dataField = responseBody.get("data");
        final JsonNode errorField = responseBody.get("error");

        assertNotNull(dataField);
        assertTrue(errorField.isNull());
        assertTrue(dataField.isObject());
        consumer.accept(dataField);
    }

    /*
    * DataJpaTest - UserEntity
    * 항상 UserStatus.ACTIVE
    * */
    public static void assertDataJpaTestUserEntityEquals(UserEntity actual, UserEntity expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getRole()).isEqualTo(expected.getRole());
        assertThat(actual.getNickname()).isEqualTo(expected.getNickname());
        assertThat(actual.getProfileImgUrl()).isEqualTo(expected.getProfileImgUrl());
        assertThat(actual.getIntroduce()).isEqualTo(expected.getIntroduce());
        assertThat(actual.getIsAgreement()).isEqualTo(expected.getIsAgreement());
        assertThat(UserStatus.ACTIVE).isEqualTo(expected.getStatus());
        assertThat(actual.getCreatedAt()).isEqualTo(expected.getCreatedAt());
        assertThat(actual.getUpdatedAt()).isEqualTo(expected.getUpdatedAt());
    }
}
