package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDTO> listCustomers();

    Optional<CustomerDTO> getCustomerById(UUID customerId);

    List<CustomerDTO> getAllCustomers();

    CustomerDTO saveNewCusty(CustomerDTO customer);

    Optional<CustomerDTO> updateCustyById(UUID customerId, CustomerDTO customer);

    Boolean deleteCustyById(UUID customerId);

    Optional<CustomerDTO> patchCustyById(UUID customerId, CustomerDTO customer);
}
