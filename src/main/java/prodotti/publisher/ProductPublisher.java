package prodotti.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import prodotti.dto.audit.ProductEventDTO;
import prodotti.utils.RedisTopics;

@Service
public class ProductPublisher extends PublisherCommon<ProductEventDTO> {
    public ProductPublisher(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, RedisTopics.PRODUCT_TOPIC);
    }
}
