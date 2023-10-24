/**
 * RequestQueue Class
 * 
 * The RequestQueue class functions as an interrupt handler, managing and responding to
 * interrupts that occur within the system. It provides operations to enqueue and dequeue
 * processes and also includes methods for finding, getting, and checking the status of processes.
 * 
 * Functions within the broader scope of the whole project:
 * - Manages and responds to interrupts in the system.
 * - Enqueues and dequeues processes based on their arrival times.
 * - Provides methods to find, get, and check the status of processes in the queue.
 * 
 * Usage: Used to handle interrupts and manage processes in the operating system.
 */

public class RequestQueue {

    // Round robin scheduler
    Scheduler roundRobinQueue;

    // Node class for the elements of the queue
    private Node h; // head
    private Node t; // tail

    // Constructor initializing the head and tail nodes, also initializes the roundRobinQueue
    public RequestQueue() {
        h = null;
        t = null;
        roundRobinQueue = new Scheduler();
    }

    // Node class representing elements in the queue
    public class Node {
        public PCB process; // Process Control Block
        public Node next; // Reference to the next node
    }

    // Enqueues a process into the queue
    public void enqueue(PCB process) {
        Node n = new Node();
        n.process = process;
        n.next = null;

        if (h == null && t == null) {
            h = t = n;
        } else {
            t.next = n;
            t = n;
        }
    }

    // Dequeues a process from the queue
    public void dequeue() {
        if (h == null && t == null) {
            return;
        }

        if (h == t) {
            h = t = null;
        } else {
            assert h != null;
            h = h.next;
        }
    }

    // Finds a process by its ID (hashed integer)
    boolean find(int id) {
        Node holdHead = h;

        while (holdHead != null) {
            if (Integer.parseInt(holdHead.process.id[1]) == id) {
                return true;
            }
            holdHead = holdHead.next;
        }

        return false;
    }

    // Finds a process by its name
    boolean find(String id) {
        Node holdHead = h;

        while (holdHead != null) {
            if (holdHead.process.id[0].equalsIgnoreCase(id)) {
                return true;
            }
            holdHead = holdHead.next;
        }

        return false;
    }

    // Gets a process by its ID (hashed integer)
    PCB getProcessById(int id) {
        Node holdHead = h;

        while (holdHead != null) {
            if (Integer.parseInt(holdHead.process.id[1]) == id) {
                return holdHead.process;
            }
            holdHead = holdHead.next;
        }

        return null;
    }

    // Checks if the queue is empty
    boolean empty() {
        return this.h == null;
    }

    // Retrieves the front process in the queue
    public PCB front() {
        return this.h.process;
    }

    // Custom string representation of the queue
    @Override
    public String toString() {
        Node holdHead = h;
        StringBuilder processString = new StringBuilder();

        while (holdHead != null) {
            processString.append(holdHead.process);
            holdHead = holdHead.next;
        }

        return processString.toString();
    }

    // Prints the queue elements
    void print() {
        Node holdHead = h;

        System.out.print("[ ");
        while (holdHead != null) {
            if (holdHead.next == null) {
                System.out.print(holdHead.process.id[0] + " ");
            } else {
                System.out.print(holdHead.process.id[0] + " | ");
            }

            holdHead = holdHead.next;
        }

        System.out.println("]");
    }

    // Returns a string representation of the queue elements
    String string() {
        Node holdHead = h;
        StringBuilder queue = new StringBuilder("[ ");

        while (holdHead != null) {
            if (holdHead.next == null) {
                queue.append(holdHead.process.id[0]).append(" ");
            } else {
                queue.append(holdHead.process.id[0]).append(" | ");
            }

            holdHead = holdHead.next;
        }

        queue.append("]");

        return queue.toString();
    }
}
