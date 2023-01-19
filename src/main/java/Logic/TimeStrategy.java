package Logic;

import Model.Client;
import Model.Queue;

import java.util.List;

public class TimeStrategy implements Strategy{
    @Override
    public synchronized void addClientToQueue(List<Queue> queues, Client client) {
        int min_time = Integer.MAX_VALUE;

        for(Queue q : queues){
            if(q.getWaitingTime() < min_time)
                min_time = q.getWaitingTime();
        }
        for(Queue q : queues) {
            if (q.getWaitingTime() == min_time) {
                q.addClient(client);
                System.out.println(client.toString()+ " enters " + "Queue " + q.getName());
                break;
            }
        }
    }
}
