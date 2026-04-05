package ao.co.ensa.investor.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @Value("${spring.data.redis.username:}")
    private String redisUsername;

    @Value("${spring.data.redis.url:}")
    private String redisUrl;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config;

        if (redisUrl != null && !redisUrl.isBlank()) {
            URI uri = URI.create(redisUrl);
            String host = uri.getHost() != null ? uri.getHost() : redisHost;
            int port = uri.getPort() > 0 ? uri.getPort() : redisPort;
            config = new RedisStandaloneConfiguration(host, port);

            String urlUsername = extractUserFromUri(uri);
            String urlPassword = extractPasswordFromUri(uri);

            if (urlUsername != null && !urlUsername.isBlank()) {
                config.setUsername(urlUsername);
            }
            if (urlPassword != null && !urlPassword.isBlank()) {
                config.setPassword(RedisPassword.of(urlPassword));
            }
        } else {
            config = new RedisStandaloneConfiguration(redisHost, redisPort);
        }

        if ((config.getUsername() == null || config.getUsername().isBlank())
            && redisUsername != null && !redisUsername.isBlank()) {
            config.setUsername(redisUsername);
        }

        if (!config.getPassword().isPresent() && redisPassword != null && !redisPassword.isBlank()) {
            config.setPassword(RedisPassword.of(redisPassword));
        }

        return new LettuceConnectionFactory(config);
    }

    private String extractUserFromUri(URI uri) {
        String userInfo = uri.getUserInfo();
        if (userInfo == null || userInfo.isBlank()) {
            return null;
        }

        String[] parts = userInfo.split(":", 2);
        if (parts.length == 0 || parts[0].isBlank()) {
            return null;
        }

        return URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
    }

    private String extractPasswordFromUri(URI uri) {
        String userInfo = uri.getUserInfo();
        if (userInfo == null || userInfo.isBlank()) {
            return null;
        }

        String[] parts = userInfo.split(":", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            return null;
        }

        return URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        GenericJackson2JsonRedisSerializer jsonSerializer = redisJsonSerializer();

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();
        return template;
    }

    private GenericJackson2JsonRedisSerializer redisJsonSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.activateDefaultTyping(
            BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build(),
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        );
        return new GenericJackson2JsonRedisSerializer(mapper);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        GenericJackson2JsonRedisSerializer jsonSerializer = redisJsonSerializer();

        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1))
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
            .disableCachingNullValues();

        RedisCacheConfiguration staticDataCacheConfig = cacheConfig.entryTtl(Duration.ofHours(24));
        RedisCacheConfiguration investorContentCacheConfig = cacheConfig.entryTtl(Duration.ofHours(24));
        RedisCacheConfiguration userSessionsCacheConfig = cacheConfig.entryTtl(Duration.ofMinutes(30));

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(cacheConfig)
            .withCacheConfiguration("staticData", staticDataCacheConfig)
            .withCacheConfiguration("investorContent", investorContentCacheConfig)
            .withCacheConfiguration("userSessions", userSessionsCacheConfig)
            .build();
    }
}
