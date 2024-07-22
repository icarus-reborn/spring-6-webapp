package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.entities.Cider;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CiderCSVRecord;
import guru.springframework.spring6restmvc.model.CiderDTO;
import guru.springframework.spring6restmvc.model.CiderStyle;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CiderRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.service.CiderCsvService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {


    private final CiderRepository ciderRepository;
    private final CustomerRepository customerRepository;
    private final CiderCsvService ciderCsvService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadCiderData();
        loadCsvData();
        loadCustomerData();
    }

    private void loadCsvData() throws FileNotFoundException {
    if (ciderRepository.count() <10) {
        File file = ResourceUtils.getFile("classpath:csvdata/ciders.csv");

        List<CiderCSVRecord> recs = ciderCsvService.convertCSV(file);

        recs.forEach(ciderCSVRecord -> {
            CiderStyle ciderStyle = switch (ciderCSVRecord.getStyle()){
                case "American Pale Lager" -> CiderStyle.Apple;
                case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                        CiderStyle.Scrumpy;
                case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> CiderStyle.Pear;
                case "American Porter" -> CiderStyle.Fizzy;
                case "Oatmeal Stout", "American Stout" -> CiderStyle.Fruit;
                case "Saison / Farmhouse Ale" -> CiderStyle.Fruit;
                case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> CiderStyle.Apple;
                case "English Pale Ale" -> CiderStyle.Pear;
                default -> CiderStyle.Apple;
            };

            ciderRepository.save(Cider.builder()
                    .ciderName(StringUtils.abbreviate(ciderCSVRecord.getBeer(), 50))
                    .ciderStyle(ciderStyle)
                    .price(BigDecimal.TEN)
                    .upc(ciderCSVRecord.getRow().toString())
                    .quantityOnHand(ciderCSVRecord.getCount())
                    .build());
        });
            }

    }

private void loadCiderData() {
        if (ciderRepository.count() == 0 ){
            Cider cider1 = Cider.builder()
                    .ciderName("2Magners Pear Cider")
                    .ciderStyle(CiderStyle.Pear)
                    .upc("123456")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(122)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Cider cider2 = Cider.builder()
                    .ciderName("2Magners Apple Cider")
                    .ciderStyle(CiderStyle.Apple)
                    .upc("214545")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(324)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();
            Cider cider3 = Cider.builder()
                    .ciderName("Melechiori Cidro")
                    .ciderStyle(CiderStyle.Fizzy)
                    .upc("34256")
                    .price(new BigDecimal("8.99"))
                    .quantityOnHand(57)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            ciderRepository.save(cider1);
            ciderRepository.save(cider2);
            ciderRepository.save(cider3);
        }


    }
    private void loadCustomerData() {

        if (customerRepository.count()==0){
            Customer cust1 = Customer.builder()
                    .customerName("2Billy Nomates")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now()).build();

            Customer cust2 = Customer.builder()
                    .customerName("2Willy Twomates")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer cust3 = Customer.builder()
                    .customerName("2Silly Threemates")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.saveAll(Arrays.asList(cust1,cust2,cust3));
        }

        //customerRepository.save(cust2);
        //customerRepository.save(cust3);
    }

}
