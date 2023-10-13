public interface DiningServer
{
    // called by a philosopher when it wishes to eat
    public void takeForks(int philosopherNumber);

    // called by a philosopher when it is finished eating
    public void returnForks(int philosopherNumber);


}
