package prodotti.service.product;

import prodotti.dto.audit.ProductEventDTO;
import prodotti.dto.product.ProductRequest;
import prodotti.dto.product.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse findById(final Long id);
    List<ProductResponse> findAll(Integer offset, Integer limit);
    ProductResponse create(ProductRequest productRequest);
    ProductResponse update(Long id, ProductRequest request);
    void delete(final Long id);

    //AUDIT
    void audit(final ProductEventDTO eventDTO);
}
