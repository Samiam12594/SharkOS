/**
 * FileHandler Class
 * 
 * The FileHandler class performs file reading and writing operations. It provides methods
 * to read content from a file and store it into memory, as well as to write content to an
 * output file based on the input file name.
 * 
 * Functions within the broader scope of the whole project:
 * - Reads content from a specified file and stores it into memory.
 * - Writes content to an output file based on the input file name.
 * 
 * Usage: Used to handle file reading and writing operations within the operating system.
 */
import java.io.*;

public class FileHandler {

    // BufferedReader and BufferedWriter objects for reading from and writing to files
    BufferedReader buff_reader;
    BufferedWriter buff_writer;

    /*
        Function to read a file.
        Parameters:
        - fn: file name
        - mem: array representing memory
        - St_Ad: starting address in the memory where the content will be stored
    */
    public void rf(String fn, String[] mem, int St_Ad) {
        try {
            // Create a FileReader object for the specified file
            FileReader file = new FileReader("./programs/" + fn);
            // Initialize the BufferedReader to read from the FileReader
            buff_reader = new BufferedReader(file);

            String line;
            // Read each line from the file and store it in the memory array
            while ((line = buff_reader.readLine()) != null) {
                mem[St_Ad++] = line;
            }

            // Close the BufferedReader after reading
            buff_reader.close();

        } catch (Exception error) {
            // Handle any exceptions that occur during file reading
            System.out.println(error);
        }
    }

    /*
        Function to write to a file.
        Parameters:
        - fn: file name
        - c: content to be written into the file
    */
    public void wf(String fn, String c) {
        try {
            // Define the directory where the output file will be stored
            String directory = "./outputs/";
            // Create the file name for the output file (based on the input file name)
            String filePathName = fn.split("\\.")[0] + "-output.txt";

            // Create a File object for the output file in the specified directory
            File file = new File(directory + filePathName);
            // Initialize FileWriter to write to the File object
            FileWriter fileWriter = new FileWriter(file);
            // Initialize the BufferedWriter to write to the FileWriter
            buff_writer = new BufferedWriter(fileWriter);

            // Write the content into the file
            buff_writer.write(c);

            // Close the BufferedWriter after writing
            buff_writer.close();

        } catch (Exception error) {
            // Handle any exceptions that occur during file writing
            System.out.println(error);
        }
    }
}
