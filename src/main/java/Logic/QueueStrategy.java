package Logic;

import Model.Client;
import Model.Queue;

import java.util.List;

public class QueueStrategy implements Strategy{
    @Override
    public synchronized void addClientToQueue(List<Queue> queues, Client client) {
        int min_size = Integer.MAX_VALUE;

        for(Queue q : queues){
            if(q.getSize() < min_size)
                min_size = q.getSize();
        }
        for(Queue q : queues) {
            if (q.getSize() == min_size) {
                q.addClient(client);
                System.out.println(client.toString()+ " enters " + "Queue " + q.getName());
                break;
            }
        }
    }
}
