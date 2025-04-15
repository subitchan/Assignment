package bean;

import java.time.LocalDate;
import java.time.LocalTime;

public class Movie extends Event {
    private String genre;
    private String actorName;
    private String actressName;

    public Movie() {
        super();
    }

    public Movie(String eventName, LocalDate eventDate, LocalTime eventTime,
                 Venue venue, int totalSeats, double ticketPrice,
                 String genre, String actorName, String actressName) {
        super(eventName, eventDate, eventTime, venue, totalSeats, ticketPrice, "Movie");
        this.genre = genre;
        this.actorName = actorName;
        this.actressName = actressName;
    }

    @Override
    public void displayEventDetails() {
        super.displayEventDetails();
        System.out.println("Genre: " + genre);
        System.out.println("Lead Actor: " + actorName);
        System.out.println("Lead Actress: " + actressName);
    }
}
