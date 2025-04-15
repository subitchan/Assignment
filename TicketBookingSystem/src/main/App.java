package main;

import bean.*;
import service.*;
import service.impl.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        IEventServiceProvider eventService = new EventServiceImpl();
        IBookingSystemRepository bookingService = new BookingSystemRepositoryImpl();

        while (true) {
            System.out.println("\n==== Ticket Booking System Menu ====");
            System.out.println("1. Create Event");
            System.out.println("2. Book Tickets");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Get Event Details");
            System.out.println("5. Get Booking Details");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter event name: ");
                    String eventName = sc.nextLine();
                    System.out.print("Enter date (yyyy-mm-dd): ");
                    String date = sc.nextLine();
                    System.out.print("Enter time (HH:mm): ");
                    String time = sc.nextLine();
                    System.out.print("Enter total seats: ");
                    int seats = Integer.parseInt(sc.nextLine());
                    System.out.print("Enter ticket price: ");
                    double price = Double.parseDouble(sc.nextLine());
                    System.out.print("Enter event type (Movie, Concert, Sports): ");
                    String type = sc.nextLine();
                    System.out.print("Enter venue ID: ");
                    int venueId = sc.nextInt();

//                    System.out.println("Enter venue name: ");
//                    String venueName = sc.nextLine();
//                    System.out.println("Enter venue address: ");
//                    String venueAddress = sc.nextLine();
                    Venue venue = new Venue(venueId, "", "");

                    Event event = eventService.createEvent(eventName, date, time, seats, price, type, venue);
                    System.out.println("Event created with ID: " + event.getEventId());
                }

                case 2 -> {
                    System.out.print("Enter event name: ");
                    String eventName = sc.nextLine();

                    System.out.print("Enter number of tickets: ");
                    int numTickets = Integer.parseInt(sc.nextLine());

                    System.out.print("Enter customer name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter customer email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter phone number: ");
                    String phone = sc.nextLine();

                    Customer customer = new Customer(name, email, phone);
                    List<Customer> customerList = List.of(customer);

                    Booking booking = bookingService.bookTickets(eventName, numTickets, customerList);
                    if (booking != null) {
                        System.out.println("Booking successful with booking_id="+booking.getBookingId());
                    } else {
                        System.out.println("Booking failed.");
                    }
                }

                case 3 -> {
                    System.out.print("Enter booking ID to cancel: ");
                    int id = Integer.parseInt(sc.nextLine());
                    boolean success = bookingService.cancelBooking(id);
                    if (success) {
                        System.out.println("Booking cancelled successfully.");
                    } else {
                        System.out.println("Cancellation failed.");
                    }
                }

                case 4 -> {
                    List<Event> events = eventService.getAllEvents();
                    for (Event e : events) {
                        System.out.println("----------------------------");
                        e.displayEventDetails();
                    }
                }

                case 5 -> {
                    System.out.print("Enter booking ID: ");
                    int id = Integer.parseInt(sc.nextLine());
                    Booking b = bookingService.getBookingDetails(id);
                    if (b != null) {
                        b.displayBookingDetails();
                        System.out.println(b.getCustomers()[0].getCustomerName());
                        System.out.println(b.getEvent().getEventName());
                    } else {
                        System.out.println("No booking found.");
                    }
                }

                case 6 -> {
                    System.out.println("Thank you! Exiting...");
                    sc.close();
                    return;
                }

                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
