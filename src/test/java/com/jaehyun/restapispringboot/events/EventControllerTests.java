package com.jaehyun.restapispringboot.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMpper;


  @Test
  public void createEvent() throws Exception {
    EventDto event = EventDto.builder()
            .name("Spring")
            .description("REST API Development with Spring Boot")
            .beginEnrollmentDateTime(LocalDateTime.of(2025, 11, 1, 14, 21))
            .closeEnrollmentDateTime(LocalDateTime.of(2025, 12, 1, 14, 21))
            .beginEventDateTime(LocalDateTime.of(2025, 11, 2, 14, 21))
            .endEventDateTime(LocalDateTime.of(2025, 11, 30, 14, 21))
            .basePrice(100)
            .maxPrice(200)
            .limitOfEnrollment(100)
            . location("강남역")
            .build();

    mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMpper.writeValueAsString(event)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE));

  }

  @Test
  public void createEvent_Bad_Request() throws Exception {
    Event event = Event.builder()
            .id(100)
            .name("Spring")
            .description("REST API Development with Spring Boot")
            .beginEnrollmentDateTime(LocalDateTime.of(2025, 11, 1, 14, 21))
            .closeEnrollmentDateTime(LocalDateTime.of(2025, 12, 1, 14, 21))
            .beginEventDateTime(LocalDateTime.of(2025, 11, 2, 14, 21))
            .endEventDateTime(LocalDateTime.of(2025, 11, 30, 14, 21))
            .basePrice(100)
            .maxPrice(200)
            .limitOfEnrollment(100)
            . location("강남역")
            .free(true)
            .offline(false)
            .eventStatus(EventStatus.PUBLISHED)
            .build();

    mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMpper.writeValueAsString(event)))
            .andDo(print())
            .andExpect(status().isBadRequest());

  }


}