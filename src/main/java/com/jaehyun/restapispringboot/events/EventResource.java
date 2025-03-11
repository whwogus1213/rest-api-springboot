package com.jaehyun.restapispringboot.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventResource extends RepresentationModel {
  @JsonUnwrapped
  private  Event event;

  public EventResource(Event event) {
    add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    this.event = event;
  }

  public Event getEvent() {
    return event;
  }
}
