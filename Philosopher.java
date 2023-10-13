public class Philosopher implements Runnable {
    private int philosopherNumber;
    private DiningServer server; // DiningServer object for synchronization

    public Philosopher(int philosopherNumber, DiningServer server) { // constructor
        this.philosopherNumber = philosopherNumber;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int downtime = (int) (Math.random() * 3000); // random number between 0 and 300ms
                System.out.println("Philosopher " + philosopherNumber + " took " + downtime + "ms thinking");
                Thread.sleep(downtime); // put the thread to sleep
                server.takeForks(philosopherNumber); // request forks

                int sleepTime = (int) (Math.random() * 3000);
                System.out.println("Philosopher " + philosopherNumber + " took " + sleepTime + "ms eating");
                Thread.sleep(sleepTime);
                server.returnForks(philosopherNumber); // release forks


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
