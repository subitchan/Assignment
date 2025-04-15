package service;

import bean.Booking;
import bean.Customer;
import bean.Event;
import bean.Venue;

import java.util.List;

public interface IBookingSystemRepository {

    // 1. Create a new event and store in DB
    Event createEvent(String eventName, String date, String time,
                      int totalSeats, double ticketPrice, String eventType, Venue venue);

    // 2. Retrieve all event details from the database
    List<Event> getEventDetails();

    // 3. Get available tickets for an event
    int getAvailableNoOfTickets(String eventName);

    // 4. Calculate and return booking cost
    double calculateBookingCost(Event event, int numTickets);

    // 5. Book tickets for an event and store booking/customer info in DB
    Booking bookTickets(String eventName, int numTickets, List<Customer> listOfCustomers);

    // 6. Cancel booking by booking ID and update available seats in DB
    boolean cancelBooking(int bookingId);

    // 7. Get booking details by ID
    Booking getBookingDetails(int bookingId);
}

