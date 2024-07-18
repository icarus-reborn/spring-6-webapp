package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.bootstrap.BootstrapData;
import guru.springframework.spring6restmvc.entities.Cider;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ActiveProfiles("test")
@Import({BootstrapData.class, CustomerServiceImpl.class})
class CustomerRepositoryTest {
    @Autowired
    CustomerRepository custyRepository;

    @Test
    void testSavedCusty() {
        Customer savedCusty = custyRepository.save(Customer.builder()
                .customerName("your wwer2").build());

        assertThat(savedCusty).isNotNull();
        assertThat(savedCusty.getId()).isNotNull();
    }
}