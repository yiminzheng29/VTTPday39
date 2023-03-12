package sg.edu.nus.iss.app.workshop26.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class AppConfig {

	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private int redisPort;
	@Value("${spring.redis.database}")
	private int redisDatabase;

	@Value("${spring.mongo.url}")
	private String mongoUrl;

	@Value("${spaces.secret.key}")
	private String spacesSecretKey;
	@Value("${spaces.access.key}")
	private String spacesAccessKey;

	@Value("${spaces.endpoint.url}")
	private String spacesEndpointUrl;

	@Value("${spaces.endpoint.region}")
	private String spacesRegion;
    
    @Value("${spring.redis.username}")
    private String redisUsername;

    @Value("${REDIS_PASSWORD}")
    private String redisPassword;


	@Bean("post")
	        public RedisTemplate<String, String> createRedisTemplate() {
            final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName(redisHost);
            config.setPort(redisPort);
            config.setDatabase(redisDatabase);
            config.setUsername(redisUsername);
            config.setPassword(redisPassword);
    
            final JedisClientConfiguration jedisClient = JedisClientConfiguration
                    .builder().build();
            final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
            jedisFac.afterPropertiesSet();
    
            final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(jedisFac);
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
			redisTemplate.setHashKeySerializer(new StringRedisSerializer());
			redisTemplate.setHashValueSerializer(new StringRedisSerializer());
            return redisTemplate;
        }
    


	@Bean
	public MongoTemplate createMongoTemplate() {
		MongoClient client = MongoClients.create(mongoUrl);
		return new MongoTemplate(client, "articles");
	}

	@Bean 
	public AmazonS3 createS3Client() {
		BasicAWSCredentials cred = new BasicAWSCredentials(spacesAccessKey, spacesSecretKey);
		EndpointConfiguration epConfig = new EndpointConfiguration(spacesEndpointUrl, spacesRegion);

		return AmazonS3ClientBuilder.standard()
				.withEndpointConfiguration(epConfig)
				.withCredentials(new AWSStaticCredentialsProvider(cred))
				.build();
	}
}