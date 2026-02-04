package prodotti.dto.catalog;

import lombok.Data;

@Data
public class CatalogResponse {
    private Long id;
    private String name;
    private String description;

    public CatalogResponse(Long id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

}
