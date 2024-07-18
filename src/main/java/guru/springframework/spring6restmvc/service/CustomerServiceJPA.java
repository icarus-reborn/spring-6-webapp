package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDta)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {

        return Optional.ofNullable(customerMapper.customerToCustomerDta(customerRepository.findById(customerId)
                .orElse(null)));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDta)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO saveNewCusty(CustomerDTO customer) {

        return customerMapper.customerToCustomerDta(customerRepository.save(customerMapper.customerDtaToCustomer(customer))) ;

    }

    @Override
    public Optional<CustomerDTO> updateCustyById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse( foundCustomer ->{
            foundCustomer.setCustomerName(customer.getCustomerName());
            foundCustomer.setLastModifiedDate(LocalDateTime.now());
            atomicReference.set(Optional.of(customerMapper.customerToCustomerDta(customerRepository.save(foundCustomer))));

            customerRepository.save(foundCustomer);
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteCustyById(UUID customerId) {

        if (customerRepository.existsById(customerId)){
            customerRepository.deleteById(customerId);
            return true;
        }

        return false;


    }

    @Override
    public Optional<CustomerDTO> patchCustyById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse( foundCustomer ->{
            if (StringUtils.hasText(customer.getCustomerName())){
                foundCustomer.setCustomerName(customer.getCustomerName());
            }
            //foundCustomer.setCustomerName(customer.getCustomerName());
            //foundCustomer.setLastModifiedDate(LocalDateTime.now());
            atomicReference.set(Optional.of(customerMapper.customerToCustomerDta(customerRepository.save(foundCustomer))));

            //customerRepository.save(foundCustomer);
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
