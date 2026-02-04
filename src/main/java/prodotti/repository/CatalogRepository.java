package prodotti.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prodotti.entity.Catalog;

import java.util.Optional;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    @Query("SELECT c FROM Catalog c WHERE c.isDeleted = false")
    Page<Catalog> findAllNotDeleted(Pageable pageable);

    @Query("SELECT c FORM Catalog c WHERE c.name = :name AND c.isDeleted = false")
    Optional<Catalog> findByName(@Param("name") final String name);
}
