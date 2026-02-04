package prodotti.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import prodotti.subscriber.ProductSubscriber;
import prodotti.utils.RedisTopics;

@Configuration
public class RedisSubscriberConfig {

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory factory,
            ProductSubscriber productSubscriber) {

        RedisMessageListenerContainer container =
                new RedisMessageListenerContainer();

        container.setConnectionFactory(factory);

        container.addMessageListener(
                productSubscriber,
                new ChannelTopic(RedisTopics.PRODUCT_TOPIC)
        );
        return container;
    }
}
