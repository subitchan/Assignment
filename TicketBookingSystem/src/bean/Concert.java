package bean;

import java.time.LocalDate;
import java.time.LocalTime;

public class Concert extends Event {
    private String artist;
    private String type;

    public Concert() {
        super();
    }

    public Concert(String eventName, LocalDate eventDate, LocalTime eventTime,
                   Venue venue, int totalSeats, double ticketPrice,
                   String artist, String type) {
        super(eventName, eventDate, eventTime, venue, totalSeats, ticketPrice, "Concert");
        this.artist = artist;
        this.type = type;
    }

    @Override
    public void displayEventDetails() {
        super.displayEventDetails();
        System.out.println("Artist: " + artist);
        System.out.println("Concert Type: " + type);
    }
}
