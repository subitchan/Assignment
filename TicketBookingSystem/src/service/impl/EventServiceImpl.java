package service.impl;

import bean.Event;
import bean.Venue;
import service.*;

import service.IEventServiceProvider;

import java.util.List;

public class EventServiceImpl implements IEventServiceProvider {

    private final IBookingSystemRepository repository;

    public EventServiceImpl() {
        this.repository = new BookingSystemRepositoryImpl();
    }

    @Override
    public Event createEvent(String eventName, String date, String time,
                             int totalSeats, double ticketPrice, String eventType, Venue venue) {
        return repository.createEvent(eventName, date, time, totalSeats, ticketPrice, eventType, venue);
    }

    @Override
    public List<Event> getAllEvents() {
        return repository.getEventDetails();
    }

    @Override
    public Event getEventByName(String eventName) {
        List<Event> events = repository.getEventDetails();
        for (Event e : events) {
            if (e.getEventName().equalsIgnoreCase(eventName)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public int getAvailableNoOfTickets(String eventName) {
        return repository.getAvailableNoOfTickets(eventName);
    }
}
