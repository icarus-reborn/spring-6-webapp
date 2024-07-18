package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CiderCSVRecord;
import guru.springframework.spring6restmvc.service.CiderCsvService;
import guru.springframework.spring6restmvc.service.CiderCsvServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CiderCsvServiceImplTest {

    CiderCsvService ciderCsvService = new CiderCsvServiceImpl();

    @Test
    void convertCSV() throws FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:csvdata/ciders.csv");
        List<CiderCSVRecord> recs = ciderCsvService.convertCSV(file);

        System.out.println(recs.size());

        assertThat(recs.size()).isGreaterThan(0);
    }
}
