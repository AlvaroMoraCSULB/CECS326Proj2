
/**
 * DiningServer.java
 *
 * This class contains the methods called by the  philosophers.
 *
 */
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;

public class DiningServerImpl implements DiningServer {
    private Lock lock;                   // Lock for synchronization
    private Condition[] conditions;      // Condition variables for philosophers
    private int[] state;                 // State of each philosopher (0 = thinking, 1 = hungry, 2 = eating)
    private int numPhilosophers;         // Number of philosophers
    private Semaphore[] forks;              // semaphore for forks


    public DiningServerImpl(int numPhilosophers) {
        this.numPhilosophers = numPhilosophers;
        lock = new ReentrantLock();        // Initialize the ReentrantLock
        conditions = new Condition[numPhilosophers]; // Create condition variables for each philosopher
        state = new int[numPhilosophers];  // Initialize the state array
        forks = new Semaphore[numPhilosophers];  // Creates an array of 5 semaphores



        for (int i = 0; i < numPhilosophers; i++) {
            conditions[i] = lock.newCondition();  // Initialize condition variables
            state[i] = 0; // Initialize all philosophers as thinking initially.
            forks[i] = new Semaphore(1); // Initialize all semaphores to have 1 permit
        }
    }

    public void takeForks(int philosopherNumber) throws InterruptedException {
        lock.lock(); // Acquire the lock for synchronization
        try {
            state[philosopherNumber] = 1; // Set philosopher to hungry

            // Try to acquire forks
            test(philosopherNumber);

            if (state[philosopherNumber] != 2) {
                conditions[philosopherNumber].await(); // Wait if forks are not acquired
            }
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    public void returnForks(int philosopherNumber) {
        lock.lock(); // Acquire the lock for synchronization
        try {
            forks[philosopherNumber].release(); //release right fork
            forks[(philosopherNumber+4)%5].release(); //release left fork
            state[philosopherNumber] = 0; // Set philosopher to thinking
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    private void test(int philosopherNumber) {
        if (forks[philosopherNumber].tryAcquire()){ // check if the right fork is available
            System.out.println("Fork #" + philosopherNumber + " is with philosopher " + philosopherNumber);// if right fork is aquired print the index of the fork and philosopher number
            if(forks[(philosopherNumber+4)%5].tryAcquire()) { //check if the left fork is available
                System.out.println("Fork #" + ((philosopherNumber+4)%5) + " is with philosopher " + philosopherNumber);// if left fork is aquired print the index of the fork and philosopher number
                state[philosopherNumber] = 2; // Set philosopher to eating
                System.out.println("Forks are with philosopher "+ philosopherNumber);
            }
            else {
                forks[philosopherNumber].release(); //if the philosopher couldn't acquire the left fork, release the right fork
            }
        }
        conditions[philosopherNumber].signal();
    }
}
