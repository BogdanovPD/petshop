package com.test.petshop.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.test.petshop.dto.PetDto;
import com.test.petshop.exceptions.BadRequestException;
import com.test.petshop.service.PetsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@EnableWebMvc
class PetsControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetsService petsService;
    @Autowired
    private JacksonTester<PetDto> petDtoJacksonTester;

    private XmlMapper xmlMapper = new XmlMapper();

    @Test
    @DisplayName("Test pet is added correctly with json")
    void addPetJson() throws Exception {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = getPetDto();
        petDtoResp.setId(1);
        when(petsService.addPet(petDto)).thenReturn(petDtoResp);
        String jsonReq = petDtoJacksonTester.write(petDto).getJson();
        String jsonResp = petDtoJacksonTester.write(petDtoResp).getJson();
        this.mockMvc.perform(
                post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonReq))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResp));
    }

    @Test
    @DisplayName("Test pet is added correctly with xml")
    void addPetXml() throws Exception {
        PetDto petDto = getPetDto();
        PetDto petDtoResp = getPetDto();
        petDtoResp.setId(1);
        when(petsService.addPet(petDto)).thenReturn(petDtoResp);
        String xmlReq = xmlMapper.writeValueAsString(petDto);
        String xmlResp = xmlMapper.writeValueAsString(petDtoResp);
        this.mockMvc.perform(
                post("/pet")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML)
                        .content(xmlReq))
                .andExpect(status().isOk())
                .andExpect(content().xml(xmlResp));
    }

    @Test
    @DisplayName("Test pet addition throws exception on incorrect status")
    void addPetInvalidStatus() throws Exception {
        PetDto petDto = getPetDto();
        petDto.setStatus("test");
        doThrow(BadRequestException.class).when(petsService).addPet(petDto);

        String xmlReq = xmlMapper.writeValueAsString(petDto);
        this.mockMvc.perform(
                post("/pet")
                        .contentType(MediaType.APPLICATION_XML)
                        .accept(MediaType.APPLICATION_XML)
                        .content(xmlReq))
                .andExpect(status().isMethodNotAllowed());
    }

    private PetDto getPetDto() {
        return PetDto.builder()
                .id(null)
                .category("test-category")
                .name("test-name")
                .photoUrls(List.of("test-url"))
                .status("available")
                .tags(Set.of("cat", "dog"))
                .build();
    }

}
