package LLD.DESIGN_PATTERN.STRUCTURAL_PATTERN.FACADE_PATTERN.WITHOUT;


// Service class responsible for handling payments
class PaymentService {
    public void makePayment(String accountId, double amount) {
        System.out.println("Payment of ₹" + amount + " successful for account " + accountId);
    }
}

// Service class responsible for reserving seats
class SeatReservationService {
    public void reserveSeat(String movieId, String seatNumber) {
        System.out.println("Seat " + seatNumber + " reserved for movie " + movieId);
    }
}

// Service class responsible for sending notifications
class NotificationService {
    public void sendBookingConfirmation(String userEmail) {
        System.out.println("Booking confirmation sent to " + userEmail);
    }
}

// Service class for managing loyalty/reward points
class LoyaltyPointsService {
    public void addPoints(String accountId, int points) {
        System.out.println(points + " loyalty points added to account " + accountId);
    }
}

// Service class for generating movie tickets
class TicketService {
    public void generateTicket(String movieId, String seatNumber) {
        System.out.println("Ticket generated for movie " + movieId + ", Seat: " + seatNumber);
    }
}



public class Main {
    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();
        paymentService.makePayment("user_bishwa", 1000);

        SeatReservationService seatReservationService = new SeatReservationService();
        seatReservationService.reserveSeat("movieId123", "A1");

        NotificationService notificationService = new NotificationService();
        notificationService.sendBookingConfirmation("user_bishwa@mail.com");

        LoyaltyPointsService loyaltyPointsService = new LoyaltyPointsService();
        loyaltyPointsService.addPoints("user_bishwa", 10);

        TicketService ticketService = new TicketService();
        ticketService.generateTicket("movieId123", "A1");
    }
}
