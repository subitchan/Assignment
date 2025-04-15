package service;

import bean.Event;
import bean.Venue;

import java.util.List;

public interface IEventServiceProvider {

    // Create an event with all required attributes and store it
    Event createEvent(String eventName, String date, String time,
                      int totalSeats, double ticketPrice, String eventType, Venue venue);

    // Retrieve all event details from the system
    List<Event> getAllEvents();

    // Retrieve a single event by name
    Event getEventByName(String eventName);

    // Get available number of tickets for a specific event
    int getAvailableNoOfTickets(String eventName);
}
