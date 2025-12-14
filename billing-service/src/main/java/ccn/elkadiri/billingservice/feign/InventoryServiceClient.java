package ccn.elkadiri.billingservice.feign;

import ccn.elkadiri.billingservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryServiceClient {

    @GetMapping("/products/{id}")
    Product getProductById(@PathVariable("id") String id);

    @GetMapping("/products")
    List<Product> getProducts();
}

