package guru.springframework.spring6restmvc.service;

import com.opencsv.bean.CsvToBeanBuilder;
import guru.springframework.spring6restmvc.model.CiderCSVRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class CiderCsvServiceImpl implements CiderCsvService {
    @Override
    public List<CiderCSVRecord> convertCSV(File csvFile) {

        try {
            List<CiderCSVRecord> ciderCSVRecords = new CsvToBeanBuilder<CiderCSVRecord>(new FileReader(csvFile))
                    .withType(CiderCSVRecord.class)
                    .build()
                    .parse();
            return ciderCSVRecords;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
