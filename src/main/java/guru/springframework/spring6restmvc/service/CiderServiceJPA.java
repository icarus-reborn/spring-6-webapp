package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.mappers.CiderMapper;
import guru.springframework.spring6restmvc.model.CiderDTO;
import guru.springframework.spring6restmvc.repositories.CiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CiderServiceJPA implements CiderService {

    private final CiderRepository ciderRepository;
    private final CiderMapper ciderMapper;

    @Override
    public List<CiderDTO> listCiders() {
        return ciderRepository.findAll()
                .stream()
                .map(ciderMapper::ciderToCiderDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CiderDTO> getCiderById(UUID id) {

        return Optional.ofNullable(ciderMapper.ciderToCiderDto(ciderRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public CiderDTO saveNewCider(CiderDTO cider) {

        return ciderMapper.ciderToCiderDto(ciderRepository.save(ciderMapper.ciderDtoToCider(cider))) ;
    }

    @Override
    public Optional<CiderDTO> updateCiderById(UUID ciderId, CiderDTO cider) {
        AtomicReference<Optional<CiderDTO>> atomicReference = new AtomicReference<>();

        ciderRepository.findById(ciderId).ifPresentOrElse(foundCider -> {
            foundCider.setCiderName(cider.getCiderName());
            foundCider.setCiderStyle(cider.getCiderStyle());
            foundCider.setUpc(cider.getUpc());
            foundCider.setPrice(cider.getPrice());
            atomicReference.set(Optional.of(ciderMapper.ciderToCiderDto(ciderRepository.save(foundCider))));

            ciderRepository.save(foundCider);
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID ciderId) {
        if (ciderRepository.existsById(ciderId)){
            ciderRepository.deleteById(ciderId);
            return true;
        }
        return false;

    }

    @Override
    public Optional<CiderDTO> patchCiderById(UUID ciderId, CiderDTO cider) {
        AtomicReference<Optional<CiderDTO>> atomicReference = new AtomicReference<>();

        ciderRepository.findById(ciderId).ifPresentOrElse(foundCider -> {
            if (StringUtils.hasText(cider.getCiderName())){
                foundCider.setCiderName(cider.getCiderName());
            }
            if (cider.getCiderStyle() !=null){
                foundCider.setCiderStyle(cider.getCiderStyle());
            }
            if (StringUtils.hasText(cider.getUpc())){
                foundCider.setUpc(cider.getUpc());
            }
            if (cider.getPrice() != null){
                foundCider.setPrice(cider.getPrice());
            }
            if (cider.getQuantityOnHand() !=null) {
                foundCider.setQuantityOnHand(cider.getQuantityOnHand());
            }
            atomicReference.set(Optional.of(ciderMapper
                    .ciderToCiderDto(ciderRepository.save(foundCider))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
