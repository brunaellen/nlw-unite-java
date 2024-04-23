package rocketseat.com.passin.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.event.Event;
import rocketseat.com.passin.domain.event.exceptions.EventNotFoundException;
import rocketseat.com.passin.dto.event.EventResponseDTO;
import rocketseat.com.passin.repositories.EventRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

  private static final String EVENT_ID = "1";

  @Mock
  private EventRepository eventRepository;

  @Mock
  private AttendeeService  attendeeService;

  @InjectMocks
  private EventService eventService;

  @Test
  void getEventDetail_givenValidEventId_returnsEventResponseDTO() {
    final Event event = new Event(EVENT_ID, "event", "details", "slug", 2);
    final EventResponseDTO expectedEventResponseDTO = new EventResponseDTO(event, 1);

    when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
    when(attendeeService.getAllAttendeesFromEvent(EVENT_ID)).thenReturn(List.of(new Attendee()));

    final EventResponseDTO eventDetail = eventService.getEventDetail(EVENT_ID);

    assertThat(eventDetail).isEqualTo(expectedEventResponseDTO);
  }

  @Test
  void getEventDetail_givenInvalidEventId_throwsEventNotFoundException() {
    when(eventRepository.findById(EVENT_ID)).thenThrow(EventNotFoundException.class);

    assertThrows(EventNotFoundException.class, () -> eventService.getEventDetail(EVENT_ID));
  }
}