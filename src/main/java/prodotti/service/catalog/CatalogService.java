package prodotti.service.catalog;

import prodotti.dto.catalog.CatalogRequest;
import prodotti.dto.catalog.CatalogResponse;
import prodotti.dto.product.ProductResponse;

import java.util.List;

public interface CatalogService {
    List<CatalogResponse> findAll(final Integer offset, final Integer limit);
    CatalogResponse findById(final Long id);
    CatalogResponse create(final CatalogRequest request);
    CatalogResponse update(final Long id, final CatalogRequest request);
    void delete(final Long id);

    //Find product from catalogId
    List<ProductResponse> findAllProductFromCatalogId(final Long id);
}
