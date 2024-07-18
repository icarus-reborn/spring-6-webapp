package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl(){
        this.customerMap = new HashMap<>();

        CustomerDTO cust1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Billy Nomates")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now()).build();

        CustomerDTO cust2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Willy Twomates")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO cust3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Silly Threemates")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();


        customerMap.put(cust1.getId(), cust1);
        customerMap.put(cust2.getId(), cust2);
        customerMap.put(cust3.getId(), cust3);
    }


    @Override
    public CustomerDTO saveNewCusty(CustomerDTO customer) {

        CustomerDTO newCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .customerName(customer.getCustomerName())
                .lastModifiedDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();

        customerMap.put(newCustomer.getId(), newCustomer);

        return newCustomer;
    }

    @Override
    public Optional<CustomerDTO> updateCustyById(UUID customerId, CustomerDTO customer) {

        CustomerDTO existingCusty = customerMap.get(customerId);

        existingCusty.setCustomerName(customer.getCustomerName());
        existingCusty.setVersion(customer.getVersion()+1);
        existingCusty.setLastModifiedDate(LocalDateTime.now());

        //customerMap.put(existingCusty.getId(), existingCusty);

        return Optional.of(existingCusty);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<CustomerDTO>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        return Optional.of(customerMap.get(uuid));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return List.of();
    }


    @Override
    public Boolean deleteCustyById(UUID customerId) {

        customerMap.remove(customerId);
        return null;
    }

    @Override
    public Optional<CustomerDTO> patchCustyById(UUID customerId, CustomerDTO customer) {

        CustomerDTO existingCusty = customerMap.get(customerId);

        if (StringUtils.hasText(customer.getCustomerName())){
            existingCusty.setCustomerName(customer.getCustomerName());
        }
        if (customer.getVersion() != null){
            existingCusty.setVersion(customer.getVersion());
        }
        existingCusty.setLastModifiedDate(LocalDateTime.now());
        return null;
    }
}
