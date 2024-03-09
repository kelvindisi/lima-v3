package org.icipe.lima.auth.event;

import lombok.Getter;
import org.icipe.lima.auth.entity.user.AppUser;
import org.springframework.context.ApplicationEvent;

@Getter
public class SendValidationEmailEvent extends ApplicationEvent {
  private final AppUser user;
  
  public SendValidationEmailEvent(Object source, AppUser user) {
    super(source);
    this.user = user;
  }
}
