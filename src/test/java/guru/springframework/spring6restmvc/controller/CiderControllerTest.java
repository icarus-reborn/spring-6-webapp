package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.CiderDTO;
import guru.springframework.spring6restmvc.repositories.CiderRepository;
import guru.springframework.spring6restmvc.service.CiderService;
import guru.springframework.spring6restmvc.service.CiderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

//import static org.awaitility.Awaitility.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

//import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("localmysql")
//@SpringBootTest
@WebMvcTest({CiderController.class, CiderRepository.class})
class CiderControllerTest {

    //@Autowired
    //CiderController ciderController;
    @Autowired
    CiderRepository ciderRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CiderService ciderService;

    CiderServiceImpl ciderServiceImpl; //= new CiderServiceImpl();

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CiderDTO> ciderArgumentCaptor;

    @BeforeEach
    void setup() {
        ciderServiceImpl = new CiderServiceImpl();
    }

    @Test
    void patchCider() throws Exception {
        CiderDTO cider = ciderServiceImpl.listCiders().get(0);

        Map<String, Object> ciderMap = new HashMap<>();
        ciderMap.put("ciderName", "newName");

        mockMvc.perform(patch(CiderController.CIDER_PATH_ID, cider.getId())
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ciderMap)))
                .andExpect(status().isNoContent());

        verify(ciderService).patchCiderById(uuidArgumentCaptor.capture(), ciderArgumentCaptor.capture());

        assertThat(cider.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(ciderMap.get("ciderName")).isEqualTo(ciderArgumentCaptor.getValue().getCiderName());
    }

    @Test
    void testDeleteCider() throws Exception {
        CiderDTO cider = ciderServiceImpl.listCiders().get(0);

        given(ciderService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(CiderController.CIDER_PATH_ID, cider.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        //ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(ciderService).deleteById(uuidArgumentCaptor.capture());

        assertThat(cider.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateCider() throws  Exception {
        CiderDTO cider = ciderServiceImpl.listCiders().get(0);

        given(ciderService.updateCiderById(any(), any())).willReturn(Optional.of(cider));
        mockMvc.perform(put(CiderController.CIDER_PATH_ID, cider.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cider)));

        verify(ciderService).updateCiderById(any(UUID.class), any(CiderDTO.class));
    }



    @Test
    void testUpdateCiderBlankName() throws  Exception {
        CiderDTO cider = ciderServiceImpl.listCiders().get(0);

        cider.setCiderName("");
        given(ciderService.updateCiderById(any(), any())).willReturn(Optional.of(cider));
        mockMvc.perform(put(CiderController.CIDER_PATH_ID, cider.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cider)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));

        //verify(ciderService).updateCiderById(any(UUID.class), any(CiderDTO.class));
    }



    @Test
    void testCreateNewCider() throws Exception {
       // ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.findAndRegisterModules();

        CiderDTO cider = ciderServiceImpl.listCiders().get(0);
        cider.setVersion(null);
        cider.setId(null);

        //System.out.println(objectMapper.writeValueAsString(cider));
        given(ciderService.saveNewCider(any(CiderDTO.class))).willReturn(ciderServiceImpl.listCiders().get(1));

        mockMvc.perform(post(CiderController.CIDER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cider)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }



    @Test
    void testCreateNullCider() throws Exception {

        CiderDTO ciderDTO = CiderDTO.builder().build();


        //System.out.println(objectMapper.writeValueAsString(cider));
        given(ciderService.saveNewCider(any(CiderDTO.class))).willReturn(ciderServiceImpl.listCiders().get(1));

        MvcResult mvcResult = mockMvc.perform(post(CiderController.CIDER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ciderDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testListCiders() throws Exception{
        given(ciderService.listCiders()).willReturn(ciderServiceImpl.listCiders());

        mockMvc.perform(get(CiderController.CIDER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }


    @Test
    void getCiderbyIdNotFound() throws Exception {

        given(ciderService.getCiderById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(CiderController.CIDER_PATH_ID,UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }


    @Test
    void getCiderById() throws Exception {

        CiderDTO testCider = ciderServiceImpl.listCiders().get(0);

       // given(ciderService.getCiderById(any(UUID.class))).willReturn(testCider);

        given(ciderService.getCiderById(testCider.getId())).willReturn(Optional.of(testCider));
        mockMvc.perform(get(CiderController.CIDER_PATH_ID, testCider.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCider.getId().toString())))
                .andExpect(jsonPath("$.ciderName", is(testCider.getCiderName())));//  System.out.println(ciderController.getCiderById(UUID.randomUUID()));

    }
}