package com.ameqran.cuda.inference.application.event;

public interface DomainEventPublisher {

    void publish(Object domainEvent);
}
