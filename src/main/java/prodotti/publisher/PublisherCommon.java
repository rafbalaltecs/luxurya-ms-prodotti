package prodotti.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

public abstract class PublisherCommon<T> {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    protected PublisherCommon(RedisTemplate<String, Object> redisTemplate, String redisTopic) {
        this.redisTemplate = redisTemplate;
        this.topic = new ChannelTopic(redisTopic);
    }

    public void publish(T message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
