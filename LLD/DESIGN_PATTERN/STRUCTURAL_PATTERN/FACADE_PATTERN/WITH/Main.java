package LLD.DESIGN_PATTERN.STRUCTURAL_PATTERN.FACADE_PATTERN.WITH;




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

class MovieBookingFacade{
    private PaymentService paymentService;
    private SeatReservationService seatReservationService;
    private NotificationService notificationService;
    private LoyaltyPointsService loyaltyPointsService;
    private TicketService ticketService;
    
    public MovieBookingFacade(){
        this.paymentService = new PaymentService();
        this.seatReservationService = new SeatReservationService();
        this.notificationService = new NotificationService();
        this.loyaltyPointsService = new LoyaltyPointsService();
        this.ticketService = new TicketService();
    }

    public void bookMovieTicket(String accountId, String movieId, String seatNo, double amount, int point, String userEmail){
        paymentService.makePayment(accountId, amount);
        seatReservationService.reserveSeat(movieId, seatNo);
        notificationService.sendBookingConfirmation(userEmail);
        loyaltyPointsService.addPoints(accountId, point);
        ticketService.generateTicket(movieId, seatNo);
    }
}

public class Main {
    public static void main(String[] args) {
        MovieBookingFacade movieBookingService = new MovieBookingFacade();
        movieBookingService.bookMovieTicket("user_bishwa", "movieId123", "B21", 1000, 20, "user_bishwa@mail.com");
    }
}
