package prodotti.dto.audit;

import lombok.Data;
import prodotti.entity.audit.OperationAuditEnum;

@Data
public class ProductEventDTO {
    private Long productId;
    private Double price;
    private Integer quantity;
    private Long userId;
    private String operation;
    private String operationDate;
}
