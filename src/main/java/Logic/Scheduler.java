package Logic;

import Model.Client;
import Model.Queue;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class Scheduler {
    private final List<Queue> queues;
    private final int queuesNo;
    private Strategy strategy;


    public Scheduler(int queuesNo, JTextArea simArea) {
        this.queues = new ArrayList<>();
        for(int i = 1; i <= queuesNo; i++){
            queues.add(new Queue(""+i, simArea));
        }
        this.queuesNo = queuesNo;
    }
    public void setStrategy(StrategyPolicy strategyPolicy){
        if(strategyPolicy.equals(StrategyPolicy.SHORTEST_QUEUE))
            strategy = new QueueStrategy();
        if(strategyPolicy.equals(StrategyPolicy.SHORTEST_TIME))
            strategy = new TimeStrategy();
    }
    public synchronized void sendClientToQueue(Client client){
        strategy.addClientToQueue(queues, client);
    }

    public int getQueuesTime(){
        int res = 0;
        for(Queue q : queues){
            res += q.getWaitingTime();
        }
        return res;
    }
    public float calculateAvgWaitingTime(){
        float res = 0f;
        int totalClients = 0;
        for(Queue q : queues){
            res += q.getTotatWaitTime();
            totalClients += q.getServedClients();
        }
        return res / (float)totalClients;
    }

    public boolean queuesFinished(){
        int count = 0;
        for(Queue q : queues){
            if(q.isEmpty())
                count++;
        }
        return count == queuesNo;
    }
    public void startThreads(){
        for(Queue q : queues){
            q.start();
        }
    }
    public void stopThreads(){
        for(Queue q : queues)
            q.stopThread();
    }

}
