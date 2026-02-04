package prodotti.entity.audit;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document("product_stock")
@Data
public class ProductStock{
    @Id
    private String id;
    private Long productId;
    private Integer stock;
    private Double price;
    private Long userId;
    private OperationAuditEnum operation;
    private LocalDateTime eventDate;
}
