package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
//@RequestMapping("/api/v1/customer")
public class CustomerController {

    public static final String CUSTOMER_PATH ="/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
    private final CustomerService customerService;


    @PatchMapping(CUSTOMER_PATH_ID)
    public  ResponseEntity patchCustyById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer){

        customerService.patchCustyById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustyById(@PathVariable("customerId") UUID customerId){

        customerService.deleteCustyById(customerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer){

        customerService.updateCustyById(customerId, customer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity addCusty(@RequestBody CustomerDTO customer){

        CustomerDTO savedCusty = customerService.saveNewCusty(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCusty.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listCustomers(){
        return customerService.listCustomers();
    };

    //@RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    @GetMapping(value = CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId ){

        log.debug("Get Custy bu Id - in controller");


        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    };



    }
