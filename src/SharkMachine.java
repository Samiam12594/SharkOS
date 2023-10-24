/**
 * SharkMachine Class
 * 
 * The SharkMachine class contains the main method for starting the SharkOS operating system.
 * It prompts the user if they want to start the machine, and if the user inputs 'Y' or 'y',
 * it instantiates the SharkOS class, initiating the operating system.
 * 
 * Functions within the broader scope of the whole project:
 * - Provides a user interface for starting the SharkOS operating system.
 * - Initializes the SharkOS class if the user chooses to start the machine.
 * 
 * Usage: Run the main method to start the SharkOS operating system.
 */
import java.util.Scanner;

public class SharkMachine {

    public static void main(String[] args) {

        // Create a Scanner object to receive user input
        Scanner input = new Scanner(System.in);

        // Display a message to the user, asking if they want to start the machine
        System.out.println("Hello, would you like to start the machine? (Y/N)");

        // Read user input
        String userInput = input.nextLine();

        // If the user's input is 'Y' or 'y', start the SharkOS
        if (userInput.equalsIgnoreCase("Y")) {
            // Instantiate the SharkOS class to initiate the operating system
            SharkOS sharkOS = new SharkOS();
        }

        // Close the Scanner to prevent resource leak
        input.close();
    }
}