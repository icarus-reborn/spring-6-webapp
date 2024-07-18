package guru.springframework.spring6restmvc.mappers;

import guru.springframework.spring6restmvc.entities.Cider;
import guru.springframework.spring6restmvc.model.CiderDTO;
import guru.springframework.spring6restmvc.repositories.CiderRepository;
import org.mapstruct.Mapper;

@Mapper
public interface CiderMapper {

    Cider ciderDtoToCider(CiderDTO dto);
    CiderDTO ciderToCiderDto(Cider cider);
}
