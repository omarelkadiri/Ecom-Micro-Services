package ccn.elkadiri.inventoryservice;

import ccn.elkadiri.inventoryservice.Repository.ProductRepository;
import ccn.elkadiri.inventoryservice.entities.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder()
                    .id("p-1")
                    .name("Computer")
                    .price(3200)
                    .quantity(11)
                    .build());
            productRepository.save(Product.builder()
                    .id("p-2")
                    .name("Printer")
                    .price(1299)
                    .quantity(10)
                    .build());
            productRepository.save(Product.builder()
                    .id("p-3")
                    .name("Smart Phone")
                    .price(5400)
                    .quantity(8)
                    .build());

            productRepository.findAll().forEach(p -> {
                System.out.println(p.toString());
            });
        };
    }
}
