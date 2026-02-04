package prodotti.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ProductSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;

    public ProductSubscriber(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        final LocalDateTime now = LocalDateTime.now();
        try {

        }catch (Exception e){
            log.error("Message Date: {}", now);
            log.error(e.getMessage());
        }
    }
}
