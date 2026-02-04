package prodotti.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AuditableEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "sku")
    private String sku;
    @Column(name = "price")
    private Double price;
    @Column(name = "vat")
    private Integer vat;
    @ManyToOne
    @JoinColumn(name = "catalogId")
    private Catalog catalog;
}
