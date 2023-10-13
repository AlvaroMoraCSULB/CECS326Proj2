public class DiningPhilosophers {
    public static void main(String[] args) {
        int numofphilos = 5;
        DiningServer server = new DiningServerImpl(numofphilos);
        int x = 0;
        do{
            Runnable philosopher = new Philosopher(x, server);
            Thread philoThread = new Thread(philosopher); // create a thread for each philosopher
            philoThread.start(); // start the philosopher's thread
            x++;
        }while(x < numofphilos);
    }
}
