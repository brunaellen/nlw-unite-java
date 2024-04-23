package rocketseat.com.passin.dto.event;

import lombok.Value;
import rocketseat.com.passin.domain.event.Event;


@Value
public class EventResponseDTO {
    
    EventDetailDTO event;

    public EventResponseDTO(Event event, Integer numberOfAttendees){
        this.event = new EventDetailDTO(
            event.getId(),
            event.getTitle(),
            event.getDetails(),
            event.getSlug(),
            event.getMaximumAttendees(),
            numberOfAttendees
        );
    }
}
