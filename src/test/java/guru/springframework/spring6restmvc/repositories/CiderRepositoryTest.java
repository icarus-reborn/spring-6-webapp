package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.bootstrap.BootstrapData;
import guru.springframework.spring6restmvc.entities.Cider;
import guru.springframework.spring6restmvc.model.CiderStyle;
import guru.springframework.spring6restmvc.service.CiderCsvService;
import guru.springframework.spring6restmvc.service.CiderCsvServiceImpl;
import jakarta.validation.ConstraintDefinitionException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@ActiveProfiles("test")
@DataJpaTest
@Import({BootstrapData.class, CiderCsvServiceImpl.class})
class CiderRepositoryTest {
    @Autowired
    CiderRepository ciderRepository;

    @Test
    void testSavedCiderTooLong() {

        assertThrows(ConstraintViolationException.class, () -> {
            Cider savedCider = ciderRepository.save(Cider.builder()
                    .ciderName("your eee madllong nooooooooooooooooooooooooooooooooow")
                    .ciderStyle(CiderStyle.Apple)
                    .upc("wegettingmoney")
                    .price(new BigDecimal("12.99"))
                    .build());
            ciderRepository.flush();
        });
    }


    @Test
    void testSavedCider() {
    Cider savedCider = ciderRepository.save(Cider.builder()
            .ciderName("your eee")
            .ciderStyle(CiderStyle.Apple)
            .upc("wegettingmoney")
            .price(new BigDecimal("12.99"))
            .build());

    ciderRepository.flush();

    assertThat(savedCider).isNotNull();
    assertThat(savedCider.getId()).isNotNull();
}
}