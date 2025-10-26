package ccn.elkadiri.customersevice;

import ccn.elkadiri.customersevice.entities.Customer;
import ccn.elkadiri.customersevice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class CustomerSeviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerSeviceApplication.class, args);
    }

}

@Component
class CommandLineRunnerImpl implements CommandLineRunner {
    private final CustomerRepository customerRepository;

    public CommandLineRunnerImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        customerRepository.save(Customer.builder()
                .name("Name1")
                .email("email1@example.com")
                .build());
        customerRepository.save(Customer.builder()
                .name("Name2")
                .email("email2@example.com")
                .build());
        customerRepository.save(Customer.builder()
                .name("Name3")
                .email("email3@example.com")
                .build());

        customerRepository.findAll().forEach(customer -> {
            System.out.println("-----------------");
            System.out.println(customer.getId());
            System.out.println(customer.getName());
            System.out.println(customer.getEmail());
            System.out.println("-----------------");
        });
    }
}