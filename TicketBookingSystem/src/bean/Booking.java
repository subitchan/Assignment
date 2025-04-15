package bean;

import java.time.LocalDateTime;

public class Booking {
    private int bookingId;
    private Customer[] customers;
    private Event event;
    private int numTickets;
    private double totalCost;
    private LocalDateTime bookingDate;

    public Booking(int bookingId, Customer[] customers, Event event, int numTickets, double totalCost,
			LocalDateTime bookingDate) {
		super();
		this.bookingId = bookingId;
		this.customers = customers;
		this.event = event;
		this.numTickets = numTickets;
		this.totalCost = totalCost;
		this.bookingDate = bookingDate;
	}

	public Booking() {
		super();
	}

	public Booking(Customer[] customers, Event event, int numTickets) {
        this.customers = customers;
        this.event = event;
        this.numTickets = numTickets;
        this.totalCost = event.getTicketPrice() * numTickets;
        this.bookingDate = LocalDateTime.now();
        event.bookTickets(numTickets);
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public Customer[] getCustomers() {
        return customers;
    }

    public void setCustomers(Customer[] customers) {
        this.customers = customers;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void displayBookingDetails() {
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Date: " + bookingDate);
        System.out.println("Tickets: " + numTickets);
        System.out.println("Total: ₹" + totalCost);
       

    }
}
