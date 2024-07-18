package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.model.CiderCSVRecord;

import java.io.File;
import java.util.List;

public interface CiderCsvService {
    List<CiderCSVRecord> convertCSV(File csvFile);
}
