package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.service.CustomerService;
import guru.springframework.spring6restmvc.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.core.Is.is;



@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    @MockBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setup() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void patchCusty() throws  Exception {
        CustomerDTO custy = customerServiceImpl.listCustomers().get(0);

        Map<String, Object> custyMap = new HashMap<>();
        custyMap.put("dave2", "custy-1");

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, custy.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(custyMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustyById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(custy.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(custyMap.get("dave")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());

    }

    @Test
    void deleteCusty() throws Exception {
        CustomerDTO custy = customerServiceImpl.listCustomers().get(0);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, custy.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

       // ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteCustyById(uuidArgumentCaptor.capture());

        assertThat(custy.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }
    @Test
    void updateNewCusty() throws Exception {
        CustomerDTO custy = customerServiceImpl.listCustomers().get(0);

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, custy.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(custy)));
        verify(customerService).updateCustyById(any(UUID.class), any(CustomerDTO.class));
    }

    @Test
    void testCreateNewCusty() throws Exception {
        CustomerDTO custy = customerServiceImpl.listCustomers().get(0);
        custy.setVersion(null);
        custy.setCustomerName("Dave");

        given(customerService.saveNewCusty(any(CustomerDTO.class))).willReturn(customerServiceImpl.listCustomers().get(1));


        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(custy)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }



    @Test
    void testListCusties() throws  Exception{
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getCustomerByIdNotFound() throws Exception {

        given(customerService.getCustomerById(any(UUID.class))).willThrow(NotFoundException.class);

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCusties() throws Exception {

        CustomerDTO testCusty = customerServiceImpl.listCustomers().get(0);

        given(customerService.getCustomerById(testCusty.getId())).willReturn(Optional.of(testCusty));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, testCusty.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCusty.getId().toString())))
                .andExpect(jsonPath("customerName", is(testCusty.getCustomerName())));




    }
}
