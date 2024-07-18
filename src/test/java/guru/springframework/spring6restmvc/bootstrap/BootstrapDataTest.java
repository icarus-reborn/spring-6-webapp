package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.repositories.CiderRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.service.CiderCsvService;
import guru.springframework.spring6restmvc.service.CiderCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(CiderCsvServiceImpl.class)
class BootstrapDataTest {

    @Autowired
    CiderRepository ciderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CiderCsvService csvService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setup() {
        bootstrapData = new BootstrapData(ciderRepository, customerRepository, csvService);
    }

    @Test
    void TestRun() throws Exception {
        bootstrapData.run(null);

        assertThat(customerRepository.count()).isEqualTo(2413);
        assertThat(ciderRepository.count()).isEqualTo(3);
    }


}