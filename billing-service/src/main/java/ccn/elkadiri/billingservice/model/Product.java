package ccn.elkadiri.billingservice.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;
}
