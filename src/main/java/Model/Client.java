package Model;

import java.util.Random;


public class Client {
    private final int ID;
    private final int arrivalTime;
    private int serviceTime;

    public Client(int ID, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime) {
        this.ID = ID;
        Random random = new Random();
        this.arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
        this.serviceTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
    }
    public Client(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public Client() {
        this.ID = 0;
        this.arrivalTime = 0;
        this.serviceTime = 0;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }

    public void serve(){
        this.serviceTime--;
    }

    @Override
    public String toString() {
        return "("+ID+","+arrivalTime+","+serviceTime+")";
    }
}
