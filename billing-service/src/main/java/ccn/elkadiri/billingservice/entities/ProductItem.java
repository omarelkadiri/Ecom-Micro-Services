package ccn.elkadiri.billingservice.entities;

import jakarta.persistence.*;
import lombok.*;
import ccn.elkadiri.billingservice.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    @ManyToOne
    @JoinColumn(name = "bill_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Bill bill;
    private int quantity;
    private double unitPrice;
    @Transient
    private Product product;
}
