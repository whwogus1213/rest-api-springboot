package com.jaehyun.restapispringboot.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaehyun.restapispringboot.common.TestDescription;
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
  @TestDescription("정상적으로 이벤트를 생성하는 테스트")
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
  @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
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
            .location("강남역")
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

  @Test
  @TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
  public void createEvent_Bad_request_Empty_input() throws Exception {
    EventDto eventDto = EventDto.builder().build();

    mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMpper.writeValueAsString(eventDto))
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  @TestDescription("입력값이 잘못된 경우에 에러가 발생하는 테스트")
  public void createEvent_Bad_request_Wrong_input() throws Exception {
    EventDto eventDto = EventDto.builder()
            .name("Spring")
            .description("REST API Development with Spring Boot")
            .beginEnrollmentDateTime(LocalDateTime.of(2025, 11, 1, 14, 21))
            .closeEnrollmentDateTime(LocalDateTime.of(2025, 12, 1, 14, 21))
            .beginEventDateTime(LocalDateTime.of(2025, 11, 2, 14, 21))
            .endEventDateTime(LocalDateTime.of(2025, 11, 30, 14, 21))
            .basePrice(100000)
            .maxPrice(200)
            .limitOfEnrollment(100)
            . location("강남역")
            .build();

    mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMpper.writeValueAsString(eventDto))
            )
            .andExpect(status().isBadRequest());
  }


}