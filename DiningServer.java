public interface DiningServer
{
    public DiningServerImpl(int numPhilosophers);
    // called by a philosopher when it wishes to eat
    public void takeForks(int philosopherNumber) throws InterruptedException;

    // called by a philosopher when it is finished eating
    public void returnForks(int philosopherNumber);
    private void test(int philosopherNumber);


}
