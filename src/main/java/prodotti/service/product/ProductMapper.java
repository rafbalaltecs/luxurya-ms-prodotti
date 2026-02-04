package prodotti.service.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import prodotti.dto.product.ProductResponse;
import prodotti.entity.Product;

@Service
@Slf4j
public class ProductMapper {
    public ProductResponse entityToResponse(
            final Product product
                ){
        if(product != null){
            return new ProductResponse(
                    product.getId(),
                    product.getCode(),
                    product.getName(),
                    product.getDescription(),
                    product.getSku(),
                    product.getQuantity(),
                    product.getCatalog().getId(),
                    product.getVat(),
                    product.getPrice()
            );
        }
        return null;
    }
}
