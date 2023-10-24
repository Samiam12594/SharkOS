/**
 * SharkOS Class
 * 
 * The SharkOS class represents the operating system of the system.
 * It manages processes using round-robin scheduling and executes instructions
 * based on the loaded programs. The class initializes system resources, loads programs
 * into memory, and processes jobs using a series of instructions. It also handles
 * process states, context switching, and output generation.
 * 
 * Functions within the broader scope of the whole project:
 * - Initializes system resources, including memory, file handling, and process queue.
 * - Reads program files, creates Process Control Block (PCB) objects, and adds them to the request queue.
 * - Executes instructions (ADD, SUB, LDI, LDA, STR, CBR, BRH) and manages process states.
 * - Implements round-robin scheduling to manage CPU time among processes.
 * - Outputs register values and process execution information to log files.
 * 
 * Usage: Create an instance of SharkOS to activate the operating system and process jobs.
 * The class assumes the existence of program files in the "./programs/" directory.
 */
import java.io.File;
import java.util.*;

public class SharkOS {

    // List to store Process Control Blocks
    ArrayList<PCB> pcb;

    // FileHandler instance for file operations
    FileHandler fableForge;

    // RequestQueue instance for managing process requests
    RequestQueue RequestRhapsody;

    // Array to represent system memory
    private String[] Mem;

    // Output string to store register values for logging
    static String registersOutput = "";

    // Array of program file names
    private String[] programs = {
            "program1.txt", "program2.txt", "program3.txt", "program4.txt", "program5.txt", "program6.txt"
    };

    // Constructor: Initializes resources and activates the operating system
    SharkOS() {
        this.initialize_resources();
        this.activate_OS();
    }

    // Reads program files, creates PCB objects, and adds them to the request queue
    public void process_set() {
        File[] files = new File("./programs/").listFiles();
        pcb = new ArrayList<>();
        assert files != null;
        for (File file : files) {
            if (file.isFile() && file.getName().contains("txt") && !file.getName().contains("outputs")) {
                String fileName = file.getName();
                int cpu_time = rand_time();
                PCB newProcess = new PCB(fileName, cpu_time);
                pcb.add(newProcess);
                int arrivalTime = rand_time();
                while (RequestRhapsody.roundRobinQueue.processArrivalTimes.containsKey(arrivalTime)) {
                    arrivalTime = rand_time();
                }
                RequestRhapsody.roundRobinQueue.processArrivalTimes.put(arrivalTime, newProcess);
            }
        }
    }

    // Initializes system resources
    public void initialize_resources() {
        this.Mem = new String[1024];
        this.RequestRhapsody = new RequestQueue();
        this.fableForge = new FileHandler();
        this.process_set();
    }

    // Loads a program into memory from a file
    public void L_Prog(String programFile) {
        fableForge.rf(programFile, Mem, RequestRhapsody.front().PSIAR);
    }

    // Generates a random time value
    public int rand_time() {
        int value = (int) (Math.random() * 10) + 1;
        return value;
    }

    // Activates the operating system and processes jobs using round-robin
    // scheduling
    public void activate_OS() {
        // Find the maximum arrival time of processes in the request queue
        int maxArrivalTime = Collections.max(RequestRhapsody.roundRobinQueue.processArrivalTimes.keySet());
        System.out.println("Time Quantum for All Processes in the Request Queue: " +
                RequestRhapsody.roundRobinQueue.quantumTime + " seconds\n\n");

        // Iterate through arrival times and execute processes
        for (int arrivalTime = 0; arrivalTime <= maxArrivalTime; arrivalTime += 1) {
            Map<Integer, PCB> processArrivalTimes = RequestRhapsody.roundRobinQueue.processArrivalTimes;
            if (processArrivalTimes.containsKey(arrivalTime)) {
                PCB currentProcess = processArrivalTimes.get(arrivalTime);
                if (!currentProcess.procState.equalsIgnoreCase("finished")) {
                    RequestRhapsody.enqueue(currentProcess);
                    System.out.print("[PROCESS RUNNING] Process ID: " + RequestRhapsody.front().id[0] +
                            " Arrived at " + arrivalTime + " seconds");
                    System.out.println(" with a Burst Time of " + RequestRhapsody.front().cpu_time + " seconds\n");
                }
                init_jobs(arrivalTime);
            }
        }
        System.out.println("All Jobs Completed");
    }

    // Initializes and processes jobs in the request queue
    private void init_jobs(int arrivalTime) {
        // Iterate through the request queue and execute processes
        while (!RequestRhapsody.empty()) {
            PCB currentQueueProcess = RequestRhapsody.front();
            Scheduler roundRobinQueue = RequestRhapsody.roundRobinQueue;
            L_Prog(currentQueueProcess.id[0]);

            // Check if the process can complete within the time quantum
            if (currentQueueProcess.cpu_time - roundRobinQueue.quantumTime <= 0) {
                // Execute instructions until the process completes
                while (Mem[currentQueueProcess.PSIAR] != null
                        && !Mem[currentQueueProcess.PSIAR].equalsIgnoreCase("HALT")) {
                    String opCode = Mem[currentQueueProcess.PSIAR].split(" ")[0];
                    switch (opCode) {
                        case "ADD" -> this.ADD();
                        case "SUB" -> this.SUB();
                        case "LDI" -> this.LDI();
                        case "LDA" -> this.LDA();
                        case "STR" -> this.STR();
                        case "CBR" -> this.CBR();
                        case "BRH" -> this.BRH();
                    }
                    // Update process state and registers
                    currentQueueProcess.IR = opCode;
                    roundRobinQueue.processArrivalTimes.get(arrivalTime).IR = opCode;
                    addRegistersToString();
                    currentQueueProcess.PSIAR++;
                }
                createOutput();
                // Mark process as completed and dequeue it
                currentQueueProcess.procState = "COMPLETED";
                roundRobinQueue.processArrivalTimes.get(arrivalTime).procState = "COMPLETED";
                roundRobinQueue.processArrivalTimes.put(arrivalTime, currentQueueProcess);
                System.out.println("\n[PROCESS EXECUTION / COMPLETED] Process ID: " + currentQueueProcess.id[0]);
                end_job();
                RequestRhapsody.dequeue();
                System.out.println("\t\t\t Updated System Queue: " + RequestRhapsody.string() + "\n");
            } else {
                // If the process cannot complete, yield and re-add it to the queue
                YLD(arrivalTime, currentQueueProcess, roundRobinQueue);
            }
        }
    }

    // Handles the yield state of a process
    private void YLD(int arrivalTime, PCB currentQueueProcess, Scheduler roundRobinQueue) {
        currentQueueProcess.cpu_time -= roundRobinQueue.quantumTime;
        roundRobinQueue.processArrivalTimes.get(arrivalTime).procState = "WAITING";
        currentQueueProcess.procState = "WAITING";
        roundRobinQueue.processArrivalTimes.put(arrivalTime, currentQueueProcess);
        System.out.print("[WAITING] Process ID: " + currentQueueProcess.id[0] + " ");
        System.out.println("as it has " + currentQueueProcess.cpu_time + " seconds remaining for execution.");

        // Add waiting processes to the queue if they haven't been added already
        registersOutput = "";
        for (Map.Entry<Integer, PCB> processes : roundRobinQueue.processArrivalTimes.entrySet()) {
            int id = Integer.parseInt(processes.getValue().id[1]);
            if (!processes.getValue().procState.equalsIgnoreCase("finished") && !RequestRhapsody.find(id)
                    && processes.getKey() < RequestRhapsody.front().cpu_time) {
                System.out.print("[ADDING TO QUEUE] Process ID: " + processes.getValue().id[0] +
                        " arrived at " + processes.getKey() + " seconds.");
                System.out.println(" It has a burst time of " + processes.getValue().cpu_time + " seconds.");
                RequestRhapsody.enqueue(processes.getValue());
            }
        }
        RequestRhapsody.dequeue();
        RequestRhapsody.enqueue(currentQueueProcess);
        System.out.println("\t\t\t Updated System Queue: " + RequestRhapsody.string() + "\n");

    }

    // Instruction: ADD (Addition)
    private void ADD() {
        int currDataAddress = Mem[RequestRhapsody.front().PSIAR] == null ? 0
                : Integer.parseInt(Mem[RequestRhapsody.front().PSIAR].split(" ")[1]);
        RequestRhapsody.front().SAR = currDataAddress;
        RequestRhapsody.front().SDR = Mem[RequestRhapsody.front().PSIAR] == null ? 0
                : Integer.parseInt(Mem[currDataAddress]);
        RequestRhapsody.front().TMPR = RequestRhapsody.front().SDR;
        RequestRhapsody.front().ACC += RequestRhapsody.front().SDR;
    }

    // Instruction: SUB (Subtraction)
    private void SUB() {
        int currDataAddress = Mem[RequestRhapsody.front().PSIAR] == null ? 0
                : Integer.parseInt(Mem[RequestRhapsody.front().PSIAR].split(" ")[1]);
        RequestRhapsody.front().SAR = currDataAddress;
        RequestRhapsody.front().SDR = Mem[RequestRhapsody.front().PSIAR] == null ? 0
                : Integer.parseInt(Mem[currDataAddress]);
        RequestRhapsody.front().TMPR = RequestRhapsody.front().SDR;
        RequestRhapsody.front().ACC -= RequestRhapsody.front().SDR;
    }

    // Instruction: CBR (Conditional Branch)
    public void CBR() {
        int jumpAddress = Mem[RequestRhapsody.front().PSIAR] == null ? 0
                : Integer.parseInt(Mem[RequestRhapsody.front().PSIAR].split(" ")[1]);
        if (RequestRhapsody.front().ACC == 0) {
            int psiarPrev = RequestRhapsody.front().PSIAR;
            RequestRhapsody.front().PSIAR = jumpAddress - 1;
            RequestRhapsody.front().SAR = psiarPrev;
            RequestRhapsody.front().SDR = jumpAddress;
        }
    }

    // Instruction: BRH (Branch)
    public void BRH() {
        int addrInstr = Mem[RequestRhapsody.front().PSIAR] == null ? 0
                : Integer.parseInt(Mem[RequestRhapsody.front().PSIAR].split(" ")[1]);
        int psiarPrev = RequestRhapsody.front().PSIAR;
        RequestRhapsody.front().PSIAR = addrInstr - 1;
        RequestRhapsody.front().SAR = psiarPrev;
        RequestRhapsody.front().SDR = RequestRhapsody.front().PSIAR;
    }

    // Instruction: LDA (Load Accumulator with Data)
    private void LDA() {
        int addrInstr = Mem[RequestRhapsody.front().PSIAR] == null ? 0
                : Integer.parseInt(Mem[RequestRhapsody.front().PSIAR].split(" ")[1]);
        int valAtAddr = Mem[RequestRhapsody.front().PSIAR] == null ? 0
                : Integer.parseInt(Mem[addrInstr]);
        RequestRhapsody.front().ACC = valAtAddr;
        RequestRhapsody.front().SAR = addrInstr;
        RequestRhapsody.front().SDR = addrInstr;
        RequestRhapsody.front().TMPR = addrInstr;
    }

    // Instruction: LDI (Load Immediate)
    private void LDI() {
        RequestRhapsody.front().ACC = Mem[RequestRhapsody.front().PSIAR] == null ? 0
                : Integer.parseInt(Mem[RequestRhapsody.front().PSIAR].split(" ")[1]);
        RequestRhapsody.front().SAR = RequestRhapsody.front().PSIAR;
        RequestRhapsody.front().SDR = Mem[RequestRhapsody.front().PSIAR] == null ? 0
                : Integer.parseInt(Mem[RequestRhapsody.front().PSIAR].split(" ")[1]);
    }

    // Instruction: STR (Store Accumulator to Memory)
    private void STR() {
        int currAddr = Mem[RequestRhapsody.front().PSIAR] == null ? 0
                : Integer.parseInt(Mem[RequestRhapsody.front().PSIAR].split(" ")[1]);
        this.Mem[currAddr] = RequestRhapsody.front().ACC + "";
        RequestRhapsody.front().SAR = currAddr;
        RequestRhapsody.front().SDR = this.Mem[currAddr] == null ? 0
                : Integer.parseInt(this.Mem[currAddr]);
        RequestRhapsody.front().TMPR = currAddr;
    }

    // Instruction: HALT (Halt the Program)
    private void HALT() {
        System.out.println("halting");
    }

    // Resets registers and clears memory after a job is completed
    private void end_job() {
        RequestRhapsody.front().RESET_REGISTERS();
        Arrays.fill(Mem, null);
        System.out.println("\n JOB COMPLETED");
    }

    // Adds register values to the output string for logging
    private void addRegistersToString() {
        registersOutput += "Current Instruction: " + (RequestRhapsody.front().PSIAR + 1) + " - "
                + Mem[RequestRhapsody.front().PSIAR] + "\n";
        registersOutput += "\tACC: " + RequestRhapsody.front().ACC + "\n";
        registersOutput += "\tPSIAR: " + (RequestRhapsody.front().PSIAR + 1) + "\n";
        registersOutput += "\tSAR: " + RequestRhapsody.front().SAR + "\n";
        registersOutput += "\tSDR: " + RequestRhapsody.front().SDR + "\n";
        registersOutput += "\tTMPR: " + RequestRhapsody.front().TMPR + "\n";
        registersOutput += "\tMemory Contents: " + Arrays.toString(Mem) + "\n";
        registersOutput += "___________________\n";
    }

    // Writes register values to an output file
    private void createOutput() {
        fableForge.wf(RequestRhapsody.front().id[0], registersOutput);
        registersOutput = "";
    }
}
