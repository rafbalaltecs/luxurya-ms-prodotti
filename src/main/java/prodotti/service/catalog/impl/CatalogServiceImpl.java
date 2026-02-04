package prodotti.service.catalog.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import prodotti.dto.catalog.CatalogRequest;
import prodotti.dto.catalog.CatalogResponse;
import prodotti.dto.product.ProductResponse;
import prodotti.entity.Catalog;
import prodotti.entity.Product;
import prodotti.exception.RestException;
import prodotti.repository.CatalogRepository;
import prodotti.repository.ProductRepository;
import prodotti.service.catalog.CatalogMapper;
import prodotti.service.catalog.CatalogService;
import prodotti.service.product.ProductMapper;
import prodotti.utils.MethodUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;
    private final ProductRepository productRepository;
    private final CatalogMapper catalogMapper;
    private final ProductMapper productMapper;

    public CatalogServiceImpl(CatalogRepository catalogRepository, ProductRepository productRepository, CatalogMapper catalogMapper, ProductMapper productMapper) {
        this.catalogRepository = catalogRepository;
        this.productRepository = productRepository;
        this.catalogMapper = catalogMapper;
        this.productMapper = productMapper;
    }

    @Override
    public List<CatalogResponse> findAll(Integer offset, Integer limit) {
        final List<CatalogResponse> responses = new ArrayList<>();
        final Page<Catalog> catalogs = catalogRepository.findAllNotDeleted(MethodUtils.getPagination(offset, limit));
        if(!catalogs.isEmpty()){
            for(final Catalog catalog: catalogs){
                responses.add(
                        catalogMapper.entityToResponse(catalog)
                );
            }
        }
        return responses;
    }

    @Override
    public CatalogResponse findById(Long id) {
        final Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        if(optionalCatalog.isEmpty()){
            throw new RestException("Catalog Not Found");
        }
        return catalogMapper.entityToResponse(optionalCatalog.get());
    }

    @Override
    @Transactional
    public CatalogResponse create(CatalogRequest request) {
        final Optional<Catalog> optionalCatalog = catalogRepository.findByName(request.getName());
        if(optionalCatalog.isPresent()){
            throw new RestException("Catalog is exist with name: " + request.getName());
        }
        Catalog catalog = new Catalog();
        catalog.setDescription(request.getDescription());
        catalog.setName(request.getName());
        catalog.setDeleted(Boolean.FALSE);
        catalog = catalogRepository.save(catalog);
        return catalogMapper.entityToResponse(catalog);
    }

    @Override
    @Transactional
    public CatalogResponse update(Long id, CatalogRequest request) {
        final Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        if(optionalCatalog.isEmpty()){
            throw new RestException("Catalog Not Found");
        }

        final Optional<Catalog> optionalCatalogWithName = catalogRepository.findByName(request.getName());
        if(optionalCatalogWithName.isPresent()){
            if(!optionalCatalogWithName.get().getId().equals(id)){
                throw new RestException("Catalog is exist with name: " + request.getName());
            }
        }

        optionalCatalog.get().setName(request.getName());
        optionalCatalog.get().setDescription(request.getDescription());
        optionalCatalog.get().setDeleted(Boolean.FALSE);

        return catalogMapper.entityToResponse(optionalCatalog.get());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        final Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        if(optionalCatalog.isEmpty()){
            throw new RestException("Catalog Not Found");
        }
        optionalCatalog.get().setDeleted(Boolean.TRUE);
        catalogRepository.save(optionalCatalog.get());
        final List<Product> products = productRepository.findAllFromCatalogId(id);
        if(!products.isEmpty()){
            for(final Product product: products){
                product.setDeleted(Boolean.TRUE);
                productRepository.save(product);
            }
        }
    }

    @Override
    public List<ProductResponse> findAllProductFromCatalogId(Long id) {
        final Optional<Catalog> optionalCatalog = catalogRepository.findById(id);
        if(optionalCatalog.isEmpty()){
            throw new RestException("Catalog not found");
        }

        final List<ProductResponse> productResponses = new ArrayList<>();
        final List<Product> products = productRepository.findAllFromCatalogId(id);
        if(!products.isEmpty()){
            products.forEach(product -> {
                productResponses.add(
                        productMapper.entityToResponse(product)
                );
            });
        }
        return productResponses;
    }
}
