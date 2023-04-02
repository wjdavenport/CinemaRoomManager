package cinema;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Cinema {

    public static String[][] seatTracker;
    public static Scanner scanner = new Scanner(System.in);
    public static double percentageSold = 0;
    public static DecimalFormat prettyPercent = new DecimalFormat("0.00");
    public static int totalTicketsAvailable = 0;
    public static int ticketsSold = 0;
    public static int currentIncome = 0;
    public static int totalIncome = 0;

    public static void main(String[] args) {

        // Set up cinema
        int[] cinemaLayout = gatherCinemaInfo();
        int totalRows = cinemaLayout[0];
        int seatsInRows = cinemaLayout[1];
        String[][] seatTracker = new String[totalRows + 1][seatsInRows + 1];
        for (int i = 0; i < totalRows + 1; i++) {
            for (int j = 0; j < seatsInRows + 1; j++)
                seatTracker[i][j] = "S ";
        }

        // Statistics for the cinema
        totalIncome = calculateTotalIncome(totalRows, seatsInRows);
        int numberOfPurchasedTickets = 0;
        totalTicketsAvailable = totalRows * seatsInRows;
        float percentageSold = 0.0F;



        // Control logic for showing seats and buying tickets
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            int input = scanner.nextInt();
            switch (input) {
                case 0 : exit = true;
                break;
                case 1: seatPlan(totalRows, seatsInRows, seatTracker);
                break;
                case 2: buyATicket(totalRows, seatsInRows, seatTracker);
                break;
                case 3: showStats();
                break;
            }
        }
    }

    /**
     * The showStats() method prints out current information based on
     * the total number of seats and seats sold.
     */
    static void showStats() {
        System.out.println();
        System.out.println("Number of purchased tickets: " + ticketsSold);
        percentageSold = ticketsSold * 100.0 / totalTicketsAvailable;
        System.out.println("Percentage: " + prettyPercent.format(percentageSold) + "%");
        System.out.println("Current income: $" + currentIncome);
        System.out.println("Total income: $" + totalIncome);
        System.out.println();
    }

    /**
     * This method uses the number of rows and total rows to
     * calculate the total income possible if all seats are sold;
     * includes discount pricing in theaters with more than 60 seats
     * in the back half of the rows.
     */
    public static int calculateTotalIncome(int totalRows, int seatsInRow) {

        if (totalRows * seatsInRow <= 60) {
            return 10 * totalRows * seatsInRow;
        } else {
            int fullPriceRows = 10 * (totalRows / 2) * seatsInRow;
            int halfPriceRows = 8 * (totalRows - (totalRows / 2)) * seatsInRow;
            return fullPriceRows + halfPriceRows;
        }

    }

    /**
     * The seatPlan() method takes user input
     * to structure a cinema room and print
     * it out.
     * @param rows - integer number of rows in cinema
     * @param seatsPerRow - integer number of seats per row
     * @param seatTracker - String array tracking current seat
     *                    tickets for sale and bought
     */
    static void seatPlan(int rows, int seatsPerRow, String[][] seatTracker) {

        // Print out descriptor
        System.out.println();
        System.out.println("Cinema:");
        System.out.print("  ");
        for (int i = 1; i <= seatsPerRow; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // Print seat by rows
        for (int row = 1; row <= rows; row++) {
            System.out.print(row + " ");
            String seatLabel;
            for (int col = 1; col <= seatsPerRow; col++) {
                seatLabel = seatTracker[row][col];
                System.out.print(seatLabel);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * The buyATicket() method gets user input to
     * choose one seat and modify the seat in the
     * seating tracker variable.
     *
     * @param totalRows - int number of rows in cinema
     * @param seatsInRow - number of seats in a row
     * @param seatTracker - String[][] containing current
     *                    information about seats in the
     *                    theater.
     */
    static void buyATicket(int totalRows, int seatsInRow, String[][] seatTracker) {
        // Logic control loop
        boolean thisPurchaseComplete = false;
        boolean validPurchaseMade = false;
        int ticketRow = 0;
        int seatNumber = 0;
        while (!thisPurchaseComplete) {

            // Gather user input to pick one seat
            System.out.println("Enter a row number: ");
            ticketRow = scanner.nextInt();
            System.out.println("Enter a seat number in that row:");
            seatNumber = scanner.nextInt();

            // Error checking: is this a valid seat?
            if (totalTicketsAvailable - ticketsSold == 0) {
                System.out.println("Sorry, this show is sold out!");
                thisPurchaseComplete = true;
                // Error checking: is the seat already sold?
            } else if (ticketRow < 1 || ticketRow > totalRows ||
                    seatNumber < 1 || seatNumber > seatsInRow) {
                System.out.println("Wrong input!");
            } else if ("B ".equals(seatTracker[ticketRow][seatNumber])) {
                System.out.println("That ticket has already been purchased!");
            } else {
                thisPurchaseComplete = true;
                validPurchaseMade = true;
            }
        }

        // Calculate ticket price & update ticket stats
        if (validPurchaseMade) {
            int ticketPrice = findTicketPrice(totalRows, seatsInRow, ticketRow);
            System.out.println("Ticket price: $" + ticketPrice);
            currentIncome += ticketPrice;
            ticketsSold += 1;
            percentageSold = ticketsSold / totalTicketsAvailable * 100.0;
            System.out.println();
        }

        // Mark bought seat location in the theater tracker
        seatTracker[ticketRow][seatNumber] = "B ";
    }

    /**
     * The findTicketPrice() method uses total rows in cinema
     * and the particular ticket's row to calculate the price.
     * @param totalRows, an integer value for total rows of
     *                   seats in the cinema.
     * @param ticketRow, an integer for the row of the purchased
     *                   ticket.
     * @return the integer value of the ticket price
     */
    static int findTicketPrice(int totalRows, int seatsInRow, int ticketRow) {
        if (ticketRow <= totalRows / 2 || totalRows * seatsInRow < 60) {
            return 10;
        } else {
            return 8;
        }
    }

    static int[] gatherCinemaInfo() {
        System.out.println("Enter the number of rows:");
        int totalRows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int seatsInRow = scanner.nextInt();
        return new int[]{totalRows, seatsInRow};
    }
}

