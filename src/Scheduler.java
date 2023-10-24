/**
 * Scheduler Class
 * 
 * The Scheduler class is a fundamental component of the Round Robin Scheduler. It tracks
 * the arrival time of each process and manages the quantum time allocated for all processes.
 * 
 * Functions within the broader scope of the whole project:
 * - Tracks the arrival time (AT) of processes and manages quantum time (QT) allocated for all processes.
 * 
 * Usage: Used as part of the Round Robin Scheduler to manage process arrival times and quantum time.
 */
import java.util.HashMap;
import java.util.Map;

public class Scheduler {

    // Default quantum time allocated for each process is 3 seconds
    public int quantumTime = 3;

    // Holds the arrival times of processes in a map
    public Map<Integer, PCB> processArrivalTimes;

    // Constructor initializes the data structure for tracking arrival times
    Scheduler() {
        processArrivalTimes = new HashMap<>();
    }
}
