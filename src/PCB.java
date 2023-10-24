/**
 * PCB Class
 * 
 * The PCB (Process Control Block) class represents essential data for managing and controlling processes within the system.
 * It encapsulates various registers and information related to a process, including its state, program counter, and other
 * critical components. The class provides methods to reset registers and initialize the PCB object.
 * 
 * Functions within the broader scope of the whole project:
 * - Represents a process and its state within the operating system.
 * - Stores registers such as ACC, PSIAR, SAR, SDR, TMPR, CSIAR, IR, and MIR for a process.
 * - Provides methods to reset registers and initialize the PCB object.
 * 
 * Usage: Used to manage and control individual processes in the operating system.
 */
import java.util.Arrays;

public class PCB {

    // Instance variables representing different aspects of a process
    public String[] id; // holds the file name and its hashed integer name
    public int ACC = 0; // Accumulator register
    public int PSIAR = 210; // Program Status Information and Address Register
    public int SAR = 0; // Storage Address Register
    public int SDR = 0; // Storage Data Register
    public int TMPR = 0; // Temporary Register
    public int CSIAR = 0; // Current State Information and Address Register
    public String IR = ""; // Instruction Register (string representation)
    public int MIR = 0; // Memory Instruction Register (integer representation)

    public String procState = "READY"; // Process state, initialized to "READY"
    public int cpu_time; // Burst time for the process to complete

    // Overrides the toString() method to provide a custom string representation of the PCB object
    @Override
    public String toString() {
        return "Process { " +
                " id=" + Arrays.toString(id) +
                ", ACC=" + ACC +
                ", PSIAR=" + PSIAR +
                ", SAR=" + SAR +
                ", SDR=" + SDR +
                ", TMPR=" + TMPR +
                ", CSIAR=" + CSIAR +
                ", IR=" + IR +
                ", MIR=" + MIR +
                ", Process State = '" + procState + '\'' +
                " }";
    }

    /*
        Resets all the registers and instruction-related variables to their initial values.
    */
    public void RESET_REGISTERS() {
        this.ACC = this.PSIAR = this.SAR = this.SDR = this.TMPR = this.CSIAR = this.MIR = 0;
        this.IR = "";
    }

    /*
        Constructor for the PCB class.
        Initializes the id array with the process name and its hashed value, and sets the burst time for the process.
    */
    PCB(String procName, int cpu_time) {
        this.id = new String[2];
        this.id[0] = procName; // process name
        this.id[1] = String.valueOf(procName.hashCode()); // hashed value of the process name
        this.cpu_time = cpu_time; // burst time for the process to complete
    }
}

