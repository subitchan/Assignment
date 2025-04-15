package service;

import bean.Booking;
import bean.Customer;

import java.util.List;

public interface IBookingSystemServiceProvider {

    // Book tickets for an event using event name and list of customers
    Booking bookTickets(String eventName, int numTickets, List<Customer> customers);

    // Cancel a booking using its booking ID
    boolean cancelBooking(int bookingId);

    // Get full booking details by ID
    Booking getBookingDetails(int bookingId);
}
