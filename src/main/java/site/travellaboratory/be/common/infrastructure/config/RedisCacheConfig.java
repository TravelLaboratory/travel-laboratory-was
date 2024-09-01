package site.travellaboratory.be.common.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisCacheConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Object> customJacksonSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        RedisCacheConfiguration redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            // Key를 String으로 직렬화 후 저장
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            // Value를 Json으로 직렬화 후 저장
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(customJacksonSerializer))
            .entryTtl(Duration.ofMinutes(1L));

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(redisCacheConfig)
            .withCacheConfiguration("weeklyLikes", redisCacheConfig.entryTtl(Duration.ofHours(6))) // weeklyLikes TTL 6시간
            .withCacheConfiguration("dailyViews", redisCacheConfig.entryTtl(Duration.ofHours(1))) // dailyViews TTL 1시간
            .build();
    }
}
