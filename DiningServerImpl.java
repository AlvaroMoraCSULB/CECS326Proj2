/**
 * DiningServer.java
 *
 * This class contains the methods called by the  philosophers.
 *
 */
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningServerImpl implements DiningServer {
    private Lock lock;                   // Lock for synchronization
    private Condition[] conditions;      // Condition variables for philosophers
    private int[] state;                 // State of each philosopher (0 = thinking, 1 = hungry, 2 = eating)
    private int numPhilosophers;         // Number of philosophers

    public DiningServerImpl(int numPhilosophers) {
        this.numPhilosophers = numPhilosophers;
        lock = new ReentrantLock();        // Initialize the ReentrantLock
        conditions = new Condition[numPhilosophers]; // Create condition variables for each philosopher
        state = new int[numPhilosophers];  // Initialize the state array

        int i = 0;
        do{
            conditions[i] = lock.newCondition();  // Initialize condition variables
            state[i] = 0; // Initialize all philosophers as thinking initially.
            i++;
        }while(i < numPhilosophers);
    }

    @Override
    public void takeForks(int philosopherNumber) {
        lock.lock(); // Acquire the lock for synchronization
        try {
            state[philosopherNumber] = 1; // Set philosopher to

            // Try to acquire forks
            test(philosopherNumber);

            if (state[philosopherNumber] != 2) {
                try {
                    conditions[philosopherNumber].await(); // Wait if forks are not acquired
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    @Override
    public void returnForks(int philosopherNumber) {
        lock.lock(); // Acquire the lock for synchronization
        try {
            state[philosopherNumber] = 0; // Set philosopher to thinking
            test((philosopherNumber + numPhilosophers - 1) % numPhilosophers); // Check left neighbor
            test((philosopherNumber + 1) % numPhilosophers); // Check right neighbor
        } finally {
            lock.unlock(); // Release the lock
        }
    }

    private void test(int philosopherNumber) {
        if (state[philosopherNumber] == 1 && state[(philosopherNumber + numPhilosophers - 1) % numPhilosophers] != 2
                && state[(philosopherNumber + 1) % numPhilosophers] != 2) {
            state[philosopherNumber] = 2; // Set philosopher to eating
            conditions[philosopherNumber].signal();
        }
    }
}
