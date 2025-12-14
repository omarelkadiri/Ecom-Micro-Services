package ccn.elkadiri.billingservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    private Long id;
    @JsonProperty("name")
    private String name;
    private String email;
}
