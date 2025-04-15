package bean;

import java.time.LocalDate;
import java.time.LocalTime;

public abstract class Event {
    private int eventId;
    private String eventName;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private Venue venue;
    private int totalSeats;
    private int availableSeats;
    private double ticketPrice;
    private String eventType;

    public Event() {}

    public Event(String eventName, LocalDate eventDate, LocalTime eventTime,
                 Venue venue, int totalSeats, double ticketPrice, String eventType) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.venue = venue;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.ticketPrice = ticketPrice;
        this.eventType = eventType;
    }

    // Getters and setters...

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalTime eventTime) {
        this.eventTime = eventTime;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void bookTickets(int numTickets) {
        if (availableSeats >= numTickets) {
            availableSeats -= numTickets;
        }
    }

    public void cancelBooking(int numTickets) {
        availableSeats += numTickets;
    }

    public int getBookedNoOfTickets() {
        return totalSeats - availableSeats;
    }

    public double calculateTotalRevenue() {
        return getBookedNoOfTickets() * ticketPrice;
    }

    public void displayEventDetails() {
        System.out.println("Event: " + eventName);
        System.out.println("Date: " + eventDate);
       System.out.println("Time: " + eventTime);
        System.out.println("Type: " + eventType);
      System.out.println("Venue: " + venue.getVenueId());
        System.out.println("Available Seats: " + availableSeats);
        System.out.println("Price: ₹" + ticketPrice);
    }
}
