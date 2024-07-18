package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.model.CiderDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CiderService {

    List<CiderDTO> listCiders();

    Optional<CiderDTO> getCiderById(UUID id);


    CiderDTO saveNewCider(CiderDTO cider);

    Optional<CiderDTO> updateCiderById(UUID ciderId, CiderDTO cider);

    Boolean deleteById(UUID ciderId);

    Optional<CiderDTO> patchCiderById(UUID ciderId, CiderDTO cider);
}
