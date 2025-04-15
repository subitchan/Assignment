package service.impl;

import bean.*;

import exception.*;
import service.*;
import util.DBConnUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingSystemRepositoryImpl implements IBookingSystemRepository {

    @Override
    public Event createEvent(String eventName, String date, String time,
                             int totalSeats, double ticketPrice, String eventType, Venue venue) {

        String sql = "INSERT INTO event (event_name, event_date, event_time, venue_id, total_seats, available_seats, ticket_price, event_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        	) {

            ps.setString(1, eventName);
            ps.setDate(2, Date.valueOf(LocalDate.parse(date)));
            ps.setTime(3, Time.valueOf(LocalTime.parse(time)));
            ps.setInt(4, venue.getVenueId());
            ps.setInt(5, totalSeats);
            ps.setInt(6, totalSeats);
            ps.setDouble(7, ticketPrice);
            ps.setString(8, eventType);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                int eventId = rs.getInt(1);
                Event event = new Event() {}; // Replace with actual subclass as needed
                event.setEventId(eventId);
                event.setVenue(venue);
                return event;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Event> getEventDetails() {
        List<Event> eventList = new ArrayList<>();
        String sql = "SELECT * FROM event";

        try (Connection conn = DBConnUtil.getDbConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Event e = new Event() {}; // Replace with appropriate subclass if needed
                e.setEventId(rs.getInt("event_id"));
                e.setEventName(rs.getString("event_name"));
                e.setEventDate(rs.getDate("event_date").toLocalDate());
                e.setEventTime(rs.getTime("event_time").toLocalTime());
                e.setTotalSeats(rs.getInt("total_seats"));
                e.setAvailableSeats(rs.getInt("available_seats"));
                e.setTicketPrice(rs.getDouble("ticket_price"));
                e.setEventType(rs.getString("event_type"));

                Venue v = new Venue(); // optional: load venue details
                v.setVenueId(rs.getInt("venue_id"));
                e.setVenue(v);

                eventList.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eventList;
    }

    @Override
    public int getAvailableNoOfTickets(String eventName) {
        String sql = "SELECT available_seats FROM event WHERE event_name = ?";
        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, eventName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("available_seats");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double calculateBookingCost(Event event, int numTickets) {
        return event.getTicketPrice() * numTickets;
    }

    @Override
    public Booking bookTickets(String eventName, int numTickets, List<Customer> customers) {
        if (customers == null || customers.size() != 1) {
            System.out.println("Only one customer allowed per booking.");
            return null;
        }

        Customer customer = customers.get(0);
        Event event = getEventByName(eventName);
        if (event.getAvailableSeats() < numTickets) return null;
        if(event==null)
        {
        	
        	throw new EventNotFoundException("Ivalid Event id ");
        }

        try (Connection conn = DBConnUtil.getDbConnection()) {
            conn.setAutoCommit(false);
            
            boolean cust_exist=false;
            int customerId=0;
            cust_exist=customerExist(customer.getCustomerName());
            if(!cust_exist) 
            {

            // 1. Insert customer
            String customerSql = "INSERT INTO customer (customer_name, email, phone_number) VALUES (?, ?, ?)";
         
            try (PreparedStatement ps = conn.prepareStatement(customerSql, Statement.RETURN_GENERATED_KEYS))
                {
                ps.setString(1, customer.getCustomerName());
                ps.setString(2, customer.getEmail());
                ps.setString(3, customer.getPhoneNumber());
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                customerId = rs.getInt(1);
                } 
            }
            else
            {
            	String sql = "SELECT customer_id FROM customer WHERE customer_name = ? limit 1";
         		try
         		{
         			
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1,customer.getCustomerName());
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    customerId = rs.getInt("customer_id");
                    
                    
         		}
         		catch(SQLException e)
         		{
         			e.printStackTrace();
         		}
            }

            // 2. Insert booking
            double totalCost = calculateBookingCost(event, numTickets);
            int bookingId;
            String bookingSql = "INSERT INTO booking (customer_id, event_id, num_tickets, total_cost, booking_date) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(bookingSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, customerId);
                ps.setInt(2, event.getEventId());
                ps.setInt(3, numTickets);
                ps.setDouble(4, totalCost);
                ps.setTimestamp(5, Timestamp.valueOf(java.time.LocalDateTime.now()));
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                bookingId = rs.getInt(1);
            }

            // 3. Update available seats
            String updateSeats = "UPDATE event SET available_seats = available_seats - ? WHERE event_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSeats)) {
            	//String event_id="select event_id from event where ";
                ps.setInt(1, numTickets);
                ps.setInt(2, event.getEventId());
                ps.executeUpdate();
            }

            conn.commit();
            Booking b=new Booking();
            b.setBookingId(bookingId);
           
            return b; 

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

 

	@Override
    public boolean cancelBooking(int bookingId) {
        try (Connection conn = DBConnUtil.getDbConnection()) {
            // Step 1: Find num_tickets and event_id before deleting
            int numTickets = 0;
            int eventId = 0;
            String selectSql = "SELECT event_id, num_tickets FROM booking WHERE booking_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, bookingId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    eventId = rs.getInt("event_id");
                    numTickets = rs.getInt("num_tickets");
                }
                else
                {
                	throw new BookingNotFoundException("Invalid Booking Id");
                }
            }

            // Step 2: Delete booking
            String deleteSql = "DELETE FROM booking WHERE booking_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                ps.setInt(1, bookingId);
                ps.executeUpdate();
            }

            // Step 3: Update seats
            String updateSeats = "UPDATE event SET available_seats = available_seats + ? WHERE event_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSeats)) {
                ps.setInt(1, numTickets);
                ps.setInt(2, eventId);
                ps.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Booking getBookingDetails(int bookingId) {
        String sql = "SELECT b.*, c.customer_name, c.email, c.phone_number, e.event_name " +
                     "FROM booking b " +
                     "JOIN customer c ON b.customer_id = c.customer_id " +
                     "JOIN event e ON b.event_id = e.event_id " +
                     "WHERE b.booking_id = ?";

        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Booking booking = new Booking();
                booking.setNumTickets(rs.getInt("num_tickets"));
                booking.setTotalCost(rs.getDouble("total_cost"));
                booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());

                Customer customer = new Customer();
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhoneNumber(rs.getString("phone_number"));

                Event event = new Event() {};
                event.setEventName(rs.getString("event_name"));

                booking.setCustomers(new Customer[] { customer });
                booking.setEvent(event);

                return booking;
                
               
            }
            else
            {
            	throw new BookingNotFoundException("booking not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper
    private Event getEventByName(String name) {
        String sql = "SELECT * FROM event WHERE event_name = ?";
        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Event e = new Event() {};
                e.setEventId(rs.getInt("event_id"));
                e.setAvailableSeats(rs.getInt("available_seats"));
                e.setTicketPrice(rs.getDouble("ticket_price"));
                return e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //Helper
    private boolean customerExist(String customerName) {
    	 String sql = "SELECT * FROM customer WHERE customer_name = ?";
 		try
 		{
 			Connection conn = DBConnUtil.getDbConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,customerName);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return true;
            
 		}
 		catch(SQLException e)
 		{
 			e.printStackTrace();
 		}
 		return false;
 	}
}
