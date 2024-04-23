package rocketseat.com.passin.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import rocketseat.com.passin.domain.event.Event;
import rocketseat.com.passin.domain.event.exceptions.EventNotFoundException;
import rocketseat.com.passin.dto.event.EventResponseDTO;
import rocketseat.com.passin.services.AttendeeService;
import rocketseat.com.passin.services.EventService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class EventControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EventService eventService;

  @MockBean
  private AttendeeService attendeeService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void getEvent_success() throws Exception {
    final Event event = new Event("1", "event", "details", "slug", 2);
    EventResponseDTO eventResponseDTO = new EventResponseDTO(event, 1);
    when(eventService.getEventDetail(ArgumentMatchers.any())).thenReturn(eventResponseDTO);

    ResultActions response = mockMvc.perform(get("/events/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(eventResponseDTO)));

    response
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.event.attendeesAmount",
            CoreMatchers.is(eventResponseDTO.getEvent().attendeesAmount())))
        .andExpect(jsonPath("$.event.title",
            CoreMatchers.is(eventResponseDTO.getEvent().title())))
        .andExpect(jsonPath("$.event.details",
            CoreMatchers.is(eventResponseDTO.getEvent().details())))
        .andExpect(jsonPath("$.event.slug",
            CoreMatchers.is(eventResponseDTO.getEvent().slug())))
        .andExpect(jsonPath("$.event.maximumAttendees",
            CoreMatchers.is(eventResponseDTO.getEvent().maximumAttendees())));

  }

  @Test
  void getEvent_givenInvalidEventId_throwsEventNotFoundException() throws Exception {
    when(eventService.getEventDetail(ArgumentMatchers.any())).thenThrow(EventNotFoundException.class);

    ResultActions response = mockMvc.perform(get("/events/1")
        .contentType(MediaType.APPLICATION_JSON));

    response.andExpect(status().is4xxClientError());
  }
}