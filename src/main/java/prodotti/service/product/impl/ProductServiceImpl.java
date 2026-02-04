package prodotti.service.product.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import prodotti.dto.audit.ProductEventDTO;
import prodotti.dto.product.ProductRequest;
import prodotti.dto.product.ProductResponse;
import prodotti.entity.Catalog;
import prodotti.entity.Product;
import prodotti.entity.audit.OperationAuditEnum;
import prodotti.entity.audit.ProductStock;
import prodotti.exception.RestException;
import prodotti.publisher.ProductPublisher;
import prodotti.repository.CatalogRepository;
import prodotti.repository.ProductRepository;
import prodotti.repository.audit.ProductStockRepository;
import prodotti.service.auth.AuthService;
import prodotti.service.cache.RedisCache;
import prodotti.service.product.ProductMapper;
import prodotti.service.product.ProductService;
import prodotti.utils.MethodUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CatalogRepository catalogRepository;
    private final ProductStockRepository productStockRepository;
    private final ProductMapper productMapper;
    private final ProductPublisher publisher;
    private final AuthService authService;

    public ProductServiceImpl(ProductRepository productRepository, CatalogRepository catalogRepository, ProductStockRepository productStockRepository, ProductMapper productMapper, ProductPublisher publisher, AuthService authService) {
        this.productRepository = productRepository;
        this.catalogRepository = catalogRepository;
        this.productStockRepository = productStockRepository;
        this.productMapper = productMapper;
        this.publisher = publisher;
        this.authService = authService;
    }

    @Override
    public ProductResponse findById(Long id) {
        final Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            throw new RestException("Product Not Found");
        }
        return productMapper.entityToResponse(optionalProduct.get());
    }

    @Override
    public List<ProductResponse> findAll(Integer offset, Integer limit) {
        final List<ProductResponse> productResponses = new ArrayList<>();
        final Page<Product> products = productRepository.findAllNotDeleted(MethodUtils.getPagination(offset, limit));
        if(!products.isEmpty()){
            products.forEach(product -> {
                productResponses.add(
                  productMapper.entityToResponse(product)
                );
            });
        }
        return productResponses;
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest productRequest) {

        final Optional<Product> optionalProductSku = productRepository.findBySKU(productRequest.getSku());
        if(optionalProductSku.isPresent()){
            throw new RestException("Product Exist with SKY: " + productRequest.getSku());
        }

        final Optional<Product> optionalProductCode = productRepository.findByCode(productRequest.getCode());

        if(optionalProductCode.isPresent()){
            throw new RestException("Product Exist with Code: " + productRequest.getCode());
        }

        final Optional<Catalog> optionalCatalog = catalogRepository.findById(Long.valueOf(productRequest.getCatalogId()));
        if(optionalCatalog.isEmpty()){
            throw new RestException("Catalog Not Found");
        }

        Product product = new Product();
        product.setDeleted(Boolean.FALSE);
        product.setCode(productRequest.getCode());
        product.setName(productRequest.getName());
        product.setSku(productRequest.getSku());
        product.setQuantity(productRequest.getQuantity());
        product.setDescription(productRequest.getDescription());
        product.setCatalog(optionalCatalog.get());

        product = productRepository.save(product);

        publishEvent(product, OperationAuditEnum.ADD);

        return productMapper.entityToResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest productRequest) {

        final Optional<Product> optionalProduct = productRepository.findById(id);

        if(optionalProduct.isEmpty()){
            throw new RestException("Product Not Found");
        }

        final Optional<Product> optionalProductSku = productRepository.findBySKU(productRequest.getSku());

        if(optionalProductSku.isPresent()){
            if(!optionalProductSku.get().getId().equals(id)){
                throw new RestException("Product Exist with SKY: " + productRequest.getSku());
            }
        }

        final Optional<Product> optionalProductCode = productRepository.findByCode(productRequest.getCode());

        if(optionalProductCode.isPresent()){
            if(!optionalProductCode.get().getId().equals(id)){
                throw new RestException("Product Exist with Code: " + productRequest.getCode());
            }
        }

        final Optional<Catalog> optionalCatalog = catalogRepository.findById(Long.valueOf(productRequest.getCatalogId()));
        if(optionalCatalog.isEmpty()){
            throw new RestException("Catalog Not Found");
        }

        final OperationAuditEnum operationAuditEnum = optionalProduct.get().getQuantity() < productRequest.getQuantity() ? OperationAuditEnum.REMOVE : OperationAuditEnum.ADD;

        optionalProduct.get().setDeleted(Boolean.FALSE);
        optionalProduct.get().setCode(productRequest.getCode());
        optionalProduct.get().setName(productRequest.getName());
        optionalProduct.get().setSku(productRequest.getSku());
        optionalProduct.get().setQuantity(productRequest.getQuantity());
        optionalProduct.get().setDescription(productRequest.getDescription());
        optionalProduct.get().setCatalog(optionalCatalog.get());

        productRepository.save(optionalProduct.get());

        publishEvent(optionalProduct.get(), operationAuditEnum);

        return productMapper.entityToResponse(optionalProduct.get());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        final Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isEmpty()){
            throw new RestException("Product Not Found");
        }

        optionalProduct.get().setDeleted(Boolean.TRUE);
        productRepository.save(optionalProduct.get());

        optionalProduct.get().setQuantity(0);
        publishEvent(optionalProduct.get(), OperationAuditEnum.ADD);
    }

    private void publishEvent(final Product product, final OperationAuditEnum operationAuditEnum){
        final ProductEventDTO productEventDTO = new ProductEventDTO();
        productEventDTO.setProductId(product.getId());
        productEventDTO.setOperation(operationAuditEnum.name());
        productEventDTO.setQuantity(product.getQuantity());
        productEventDTO.setPrice(product.getPrice());
        productEventDTO.setOperationDate(LocalDateTime.now().toString());
        productEventDTO.setUserId(authService.getUserDataShared().getUserId());
        publisher.publish(productEventDTO);
    }

    @Override
    public void audit(ProductEventDTO eventDTO) {
        final ProductStock productStock = new ProductStock();
        productStock.setProductId(eventDTO.getProductId());
        productStock.setPrice(eventDTO.getPrice());
        productStock.setOperation(OperationAuditEnum.valueOf(eventDTO.getOperation()));
        productStock.setEventDate(LocalDateTime.parse(eventDTO.getOperationDate()));
        productStock.setUserId(eventDTO.getUserId());
        productStockRepository.save(productStock);
    }
}
