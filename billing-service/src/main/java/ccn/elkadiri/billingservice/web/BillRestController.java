package ccn.elkadiri.billingservice.web;

import ccn.elkadiri.billingservice.entities.Bill;
import ccn.elkadiri.billingservice.feign.CustomerServiceClient;
import ccn.elkadiri.billingservice.feign.InventoryServiceClient;
import ccn.elkadiri.billingservice.model.Customer;
import ccn.elkadiri.billingservice.model.Product;
import ccn.elkadiri.billingservice.repositoriy.BillRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bills")
public class BillRestController {

    private final BillRepository billRepository;
    private final CustomerServiceClient customerServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    public BillRestController(BillRepository billRepository,
                              CustomerServiceClient customerServiceClient,
                              InventoryServiceClient inventoryServiceClient) {
        this.billRepository = billRepository;
        this.customerServiceClient = customerServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable("id") Long id) {
        Optional<Bill> optionalBill = billRepository.findById(id);
        if (optionalBill.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Bill bill = optionalBill.get();

        // enrich customer
        try {
            Customer customer = customerServiceClient.getCustomerById(bill.getCustomerId());
            bill.setCustomer(customer);
        } catch (Exception ignored) {
        }

        // enrich products for each product item
        bill.getProductItems().forEach(pi -> {
            try {
                Product product = inventoryServiceClient.getProductById(pi.getProductId());
                pi.setProduct(product);
            } catch (Exception ignored) {
            }
        });

        return ResponseEntity.ok(bill);
    }

    @GetMapping("")
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billRepository.findAll();
        // enrich each bill
        bills.forEach(bill -> {
            try {
                Customer customer = customerServiceClient.getCustomerById(bill.getCustomerId());
                bill.setCustomer(customer);
            } catch (Exception ignored) {}
            bill.getProductItems().forEach(pi -> {
                try {
                    Product product = inventoryServiceClient.getProductById(pi.getProductId());
                    pi.setProduct(product);
                } catch (Exception ignored) {}
            });
        });
        return ResponseEntity.ok(bills);
    }
}
