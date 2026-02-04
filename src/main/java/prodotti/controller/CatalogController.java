package prodotti.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prodotti.dto.catalog.CatalogRequest;
import prodotti.dto.catalog.CatalogResponse;
import prodotti.dto.product.ProductResponse;
import prodotti.service.catalog.CatalogService;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@Tag(name = "Gestione Catalogo")
@Slf4j
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping(value = "", produces = org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<CatalogResponse>> findAll(@Param("offset") Integer offset, @Param("limit") Integer limit){
        return ResponseEntity.ok(catalogService.findAll(offset, limit));
    }

    @GetMapping(value = "/{id}/products", produces = org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<ProductResponse>> findAllProductsFromCategory(@PathVariable("id") Long id){
        return ResponseEntity.ok(catalogService.findAllProductFromCatalogId(id));
    }

    @GetMapping(value = "/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CatalogResponse> findById(
            @PathVariable("id") final Long id
    ){
        return ResponseEntity.ok(
                catalogService.findById(id)
        );
    }

    @PutMapping(value = "/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CatalogResponse> update(
            @PathVariable("id") final Long id,
            @RequestBody final CatalogRequest catalogRequest
            ){
        return ResponseEntity.ok(
                catalogService.update(id, catalogRequest)
        );
    }

    @PostMapping(value = "", produces = org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CatalogResponse> save(
            @RequestBody final CatalogRequest catalogRequest
    ){
        return ResponseEntity.ok(
                catalogService.create(catalogRequest)
        );
    }

    @DeleteMapping(value = "/{id}", produces = org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void delete(@PathVariable("id") Long id){
        catalogService.delete(id);
    }

}
