package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import guru.springframework.spring6restmvc.entities.Cider;
import guru.springframework.spring6restmvc.mappers.CiderMapper;
import guru.springframework.spring6restmvc.model.CiderDTO;
import guru.springframework.spring6restmvc.repositories.CiderRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@SpringBootTest(classes = CiderControllerIT.class)
//@DataJpaTest
@ActiveProfiles("localmysql")
@SpringBootTest
class CiderControllerIT {



    @Autowired
    CiderController ciderController;

    @Autowired
    CiderRepository ciderRepository;



    @Autowired
    CiderMapper ciderMapper;



    @Autowired
    ObjectMapper objectMapper;



    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp()  {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testPatchCiderBadName() throws Exception {
        Cider cider = ciderRepository.findAll().get(0);

        Map<String, Object> ciderMap = new HashMap<>();
        ciderMap.put("ciderName", "newdddddnnnnnnnnnnnnnnnnnnnnnnnnhsksòdbvldkjhboòvih sName");

        MvcResult result = mockMvc.perform(patch(CiderController.CIDER_PATH_ID, cider.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ciderMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

       }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () ->{
            ciderController.deletedById(UUID.randomUUID());
        });
    }


    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
        Cider cider = ciderRepository.findAll().get(0);

        ResponseEntity responseEntity = ciderController.deletedById(cider.getId());

        assertThat(ciderRepository.findById(cider.getId()).isEmpty());
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () ->{
            ciderController.updateById(UUID.randomUUID(), CiderDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingCider() {
        Cider cider = ciderRepository.findAll().get(0);
        CiderDTO ciderDTO = ciderMapper.ciderToCiderDto(cider);
        ciderDTO.setId(null);
        ciderDTO.setVersion(null);
        final String ciderName = "UPDATED";
        ciderDTO.setCiderName(ciderName);

        ResponseEntity responseEntity = ciderController.updateById(cider.getId(), ciderDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Cider updatedCider = ciderRepository.findById(cider.getId()).get();
        assertThat(updatedCider.getCiderName()).isEqualTo(ciderName);
    }


    @Rollback
    @Transactional
    @Test
    void saveNewCider() {
        CiderDTO ciderDTO = CiderDTO.builder()
                .ciderName("New Cider")
                .build();

        ResponseEntity responseEntity = ciderController.handlePost(ciderDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Cider cider = ciderRepository.findById(savedUUID).get();
        assertThat(cider).isNotNull();

    }

    @Test
    void testCiderIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            ciderController.getCiderById(UUID.randomUUID());
        });
    }

    @Test
    void testListCiders() {
        Cider cider = ciderRepository.findAll().get(0);

        CiderDTO dto = ciderController.getCiderById(cider.getId());

        assertThat(dto.getId()).isNotNull();
    }
     @Test
    void testListCIders() {
         List<CiderDTO> dtos = ciderController.listCiders();

         assertThat(dtos.size()).isEqualTo(3);

     }

     @Rollback
     @Transactional
     @Test
    void testEmptyList() {
         ciderRepository.deleteAll();
         List<CiderDTO> dtos = ciderController.listCiders();

         assertThat(dtos.size()).isEqualTo(0);
     }
}