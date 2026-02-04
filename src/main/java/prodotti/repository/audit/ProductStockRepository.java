package prodotti.repository.audit;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import prodotti.entity.audit.ProductStock;

@Repository
public interface ProductStockRepository extends MongoRepository<ProductStock, String> {
}
