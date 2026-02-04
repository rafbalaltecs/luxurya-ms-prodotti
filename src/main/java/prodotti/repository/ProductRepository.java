package prodotti.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prodotti.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT c FROM Product c where c.id = :id AND c.isDeleted = false")
    Optional<Product> findByIdAndNotDeleted(@Param("id") final Long id);

    @Query("SELECT c FROM Product c where c.code = :code AND c.isDeleted = false")
    Optional<Product> findByCode(@Param("code") final String code);

    @Query("SELECT c FROM Product c where c.sku = :sku AND c.isDeleted = false")
    Optional<Product> findBySKU(@Param("sku") final String sku);

    @Query("SELECT c FROM Product c WHERE c.isDeleted = false")
    Page<Product> findAllNotDeleted(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.catalog.id = :idCatalog AND p.isDeleted = false")
    List<Product> findAllFromCatalogId(
            @Param("idCatalog") Long idCatalog
    );
}
