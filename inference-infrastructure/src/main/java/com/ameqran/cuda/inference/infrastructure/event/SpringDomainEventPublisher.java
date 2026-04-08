package com.ameqran.cuda.inference.infrastructure.event;

import com.ameqran.cuda.inference.application.event.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(Object domainEvent) {
        eventPublisher.publishEvent(domainEvent);
    }
}
