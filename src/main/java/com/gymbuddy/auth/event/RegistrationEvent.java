package com.gymbuddy.auth.event;

import com.gymbuddy.auth.persistence.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationEvent extends ApplicationEvent {

    private transient User user;
    private String applicationUrl;

    /**
     * {@inheritDoc}
     */
    public RegistrationEvent(final User user, final String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
