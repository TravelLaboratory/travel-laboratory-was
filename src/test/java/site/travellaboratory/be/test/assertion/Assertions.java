package site.travellaboratory.be.test.assertion;

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
import site.travellaboratory.be.common.exception.ErrorCodes;

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

        assertEquals(errorCodes.code, responseBody.get("code").asLong());
        assertEquals(errorCodes.message, responseBody.get("local_message").asText());
    }

    public static void assertMvcDataEquals(
        MvcResult result, Consumer<JsonNode> consumer
    ) throws UnsupportedEncodingException, JsonProcessingException {
        final String content = result.getResponse().getContentAsString();
        final JsonNode responseBody = objectMapper.readTree(content);

        assertNotNull(responseBody);
        assertTrue(responseBody.isObject());

        consumer.accept(responseBody);
    }
}
