package ccn.elkadiri.billingservice.feign;

import ccn.elkadiri.billingservice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "customer-service")
public interface CustomerServiceClient {

    @GetMapping("/customers/{id}")
    Customer getCustomerById(@PathVariable("id") Long id);

    @GetMapping("/customers")
    List<Customer> getCustomers();
}
