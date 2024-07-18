package guru.springframework.spring6restmvc.service;

import guru.springframework.spring6restmvc.model.CiderDTO;
import guru.springframework.spring6restmvc.model.CiderStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CiderServiceImpl implements CiderService {

    private Map<UUID, CiderDTO> ciderMap;

    public CiderServiceImpl(){
        this.ciderMap = new HashMap<>();

        CiderDTO cider1 = CiderDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .ciderName("Magners Pear Cider")
                .ciderStyle(CiderStyle.Pear)
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CiderDTO cider2 = CiderDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .ciderName("Magners Apple Cider")
                .ciderStyle(CiderStyle.Apple)
                .upc("214545")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(324)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        CiderDTO cider3 = CiderDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .ciderName("Melechiori Cidro")
                .ciderStyle(CiderStyle.Fizzy)
                .upc("34256")
                .price(new BigDecimal("8.99"))
                .quantityOnHand(57)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        ciderMap.put(cider1.getId(), cider1);
        ciderMap.put(cider2.getId(), cider2);
        ciderMap.put(cider3.getId(), cider3);
    }

    @Override
    public List<CiderDTO> listCiders(){
        return new ArrayList<>(ciderMap.values());
    }




    @Override
    public Optional<CiderDTO> getCiderById(UUID id) {

        log.debug("get Cider id as a service was called");
        return Optional.of(ciderMap.get(id));
       /* return Cider.builder()
                .id(id)
                .version(1)
                .ciderName("Magners Pear Cider")
                .ciderStyle(CiderStyle.Pear)
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
*/
    }

        @Override
        public CiderDTO saveNewCider(CiderDTO cider) {

        CiderDTO savedCider = CiderDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .ciderName(cider.getCiderName())
                .ciderStyle(cider.getCiderStyle())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(cider.getQuantityOnHand())
                .upc(cider.getUpc())
                .price(cider.getPrice())
                .build()
                ;

            ciderMap.put(savedCider.getId(), savedCider);

            return savedCider;
        }

    @Override
    public Optional<CiderDTO> updateCiderById(UUID ciderId, CiderDTO cider) {

        CiderDTO existingCider = ciderMap.get(ciderId) ;
        existingCider.setCiderName(cider.getCiderName());
        existingCider.setPrice(cider.getPrice());
        existingCider.setCiderStyle(cider.getCiderStyle());
        existingCider.setUpc(cider.getUpc());
        existingCider.setQuantityOnHand(cider.getQuantityOnHand());
        existingCider.setVersion(cider.getVersion()+1);
        existingCider.setUpdateDate(LocalDateTime.now());

        //ciderMap.put(existingCider.getId(), existingCider);

        return Optional.of(existingCider);
    }

    @Override
    public Boolean deleteById(UUID ciderId) {

        ciderMap.remove(ciderId);

        return true;
    }


    @Override
    public Optional<CiderDTO> patchCiderById(UUID ciderId, CiderDTO cider) {

        CiderDTO existingCider = ciderMap.get(ciderId);

        if (StringUtils.hasText(cider.getCiderName())){
            existingCider.setCiderName(cider.getCiderName());
        }
        if (cider.getCiderStyle() != null){
            existingCider.setCiderStyle(cider.getCiderStyle());
        }
        if (cider.getPrice() != null){
            existingCider.setPrice(cider.getPrice());
        }

        if (cider.getQuantityOnHand() != null){
            existingCider.setQuantityOnHand(cider.getQuantityOnHand());
        }
        if (StringUtils.hasText(cider.getUpc())){
            existingCider.setUpc(cider.getUpc());
        }
        existingCider.setUpdateDate(LocalDateTime.now());

        return null;
    }
}

