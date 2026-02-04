package prodotti.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String sku;
    private Integer quantity;
    private Long catalogId;
    private Integer vat;
    private Double price;
}
