package ccn.elkadiri.billingservice;

import ccn.elkadiri.billingservice.entities.Bill;
import ccn.elkadiri.billingservice.entities.ProductItem;
import ccn.elkadiri.billingservice.feign.CustomerServiceClient;
import ccn.elkadiri.billingservice.feign.InventoryServiceClient;
import ccn.elkadiri.billingservice.model.Customer;
import ccn.elkadiri.billingservice.model.Product;
import ccn.elkadiri.billingservice.repositoriy.BillRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(BillRepository billRepository,
                               CustomerServiceClient customerServiceClient,
                               InventoryServiceClient inventoryServiceClient) {
        return args -> {
            // try to fetch customers, fallback to dummy
            Customer c1 = null, c2 = null;
            try {
                c1 = customerServiceClient.getCustomerById(1L);
            } catch (Exception ex) {
                System.out.println("DataLoader: could not fetch customer 1: " + ex.getMessage());
            }
            try {
                c2 = customerServiceClient.getCustomerById(2L);
            } catch (Exception ex) {
                System.out.println("DataLoader: could not fetch customer 2: " + ex.getMessage());
            }

            // try to fetch products, fallback to dummy list
            List<Product> products = new ArrayList<>();
            try {
                List<Product> remote = inventoryServiceClient.getProducts();
                if (remote != null) products.addAll(remote);
            } catch (Exception ex) {
                System.out.println("DataLoader: could not fetch products: " + ex.getMessage());
            }

            // If no remote data, create fallback products
            if (products.isEmpty()) {
                products.add(Product.builder().id("p-1").name("Fallback Computer").price(3000).quantity(10).build());
                products.add(Product.builder().id("p-2").name("Fallback Printer").price(1000).quantity(5).build());
            }

            // Create bill for c1 (use fixed ID for custom billing)
            long customerId1 = 1L;
            Bill bill1 = Bill.builder()
                    .billingDate(new Date())
                    .customerId(customerId1)
                    .customer(c1)
                    .productItems(new ArrayList<>())
                    .build();
            // create product item and associate
            if (!products.isEmpty()) {
                Product p = products.get(0);
                ProductItem item = ProductItem.builder()
                        .productId(p.getId())
                        .quantity(2)
                        .unitPrice(p.getPrice())
                        .product(p)
                        .bill(bill1)
                        .build();
                bill1.getProductItems().add(item);
            }

            // save bill (cascade should persist item)
            billRepository.save(bill1);

            // Create another bill for c2
            long customerId2 = 2L;
            Bill bill2 = Bill.builder()
                    .billingDate(new Date())
                    .customerId(customerId2)
                    .customer(c2)
                    .productItems(new ArrayList<>())
                    .build();
            if (products.size() > 1) {
                Product p2 = products.get(1);
                ProductItem item2 = ProductItem.builder()
                        .productId(p2.getId())
                        .quantity(1)
                        .unitPrice(p2.getPrice())
                        .product(p2)
                        .bill(bill2)
                        .build();
                bill2.getProductItems().add(item2);
            }
            billRepository.save(bill2);

            System.out.println("DataLoader: bills created (with fallback if necessary)");
        };
    }
}
