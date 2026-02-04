package prodotti.dto.product;

import lombok.Data;

@Data
public class ProductRequest {
    private String code;
    private String name;
    private String description;
    private String sku;
    private String catalogId;
    private Integer quantity;
    private Integer vat;
    private Double price;
}
