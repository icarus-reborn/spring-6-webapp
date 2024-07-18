package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.CiderDTO;
import guru.springframework.spring6restmvc.service.CiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/v1/cider")
public class CiderController {

    public static final String CIDER_PATH ="/api/v1/cider";
    public static final String CIDER_PATH_ID = CIDER_PATH + "/{ciderId}";
    private final CiderService ciderService;



    @PatchMapping(CIDER_PATH_ID)
    public ResponseEntity patchById(@PathVariable("ciderId") UUID ciderId,@RequestBody CiderDTO cider){

        ciderService.patchCiderById(ciderId, cider);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CIDER_PATH_ID)
    public ResponseEntity deletedById(@PathVariable("ciderId") UUID ciderId){

        if (!  ciderService.deleteById(ciderId)) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping(CIDER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("ciderId") UUID ciderId, @Validated @RequestBody CiderDTO cider){

        if(ciderService.updateCiderById(ciderId, cider).isEmpty()){
            throw new NotFoundException();
        };

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CIDER_PATH)
    //@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity handlePost(@Validated @RequestBody CiderDTO cider){

        CiderDTO savedCider = ciderService.saveNewCider(cider);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CIDER_PATH + "/" + savedCider.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    //@RequestMapping(method = RequestMethod.GET)
   @GetMapping(value = CIDER_PATH)
    public List<CiderDTO> listCiders(){
        return ciderService.listCiders();
    }



    @GetMapping(value = CIDER_PATH_ID)
    public CiderDTO getCiderById(@PathVariable("ciderId") UUID ciderId){

        log.debug("Get Cider bu Id - in controller 1234");

        return ciderService.getCiderById(ciderId).orElseThrow(NotFoundException::new);
    }





}
