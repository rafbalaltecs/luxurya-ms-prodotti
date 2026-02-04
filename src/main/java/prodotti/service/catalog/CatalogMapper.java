package prodotti.service.catalog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import prodotti.dto.catalog.CatalogResponse;
import prodotti.entity.Catalog;

@Service
@Slf4j
public class CatalogMapper {

    public CatalogResponse entityToResponse(final Catalog catalog){
        if(catalog != null){
            return new CatalogResponse(
                    catalog.getId(),
                    catalog.getName(),
                    catalog.getDescription()
            );
        }
        return null;
    }

}
