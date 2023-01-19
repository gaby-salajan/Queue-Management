package Model;

import javax.swing.*;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;


public class Queue extends Thread{
    private boolean running = false;

    private java.util.Queue<Client> clients;
    private AtomicInteger waitingTime;

    private int servedClients = 0;
    private int totalWaitTime = 0;
    private final JTextArea simArea;


    public Queue(String name, JTextArea simArea) {
        super(name);
        this.clients = new LinkedList<>();
        this.waitingTime = new AtomicInteger(0);
        this.simArea = simArea;
    }

    public void addClient(Client client){
        clients.add(client);
        waitingTime.addAndGet(client.getServiceTime());
        totalWaitTime += client.getServiceTime();
    }
    public void serveClient(){
        clients.element().serve();
    }

    public int getSize() {
        return clients.size();
    }
    public int getWaitingTime() {
        return waitingTime.get();
    }

    public int getTotatWaitTime(){
        return totalWaitTime;
    }
    public int getServedClients(){
        return servedClients;
    }

    public boolean isEmpty(){
        return clients.isEmpty();
    }

    public void stopThread() {
        running = false;
    }


    public void run() {
        int time = 0;
        int prevTime = 1;
        running = true;
        while(running){
            time++;
            if(!clients.isEmpty()) {
                if (clients.element().getServiceTime() == 0) {
                    synchronized (System.out) {
                        System.out.println(clients.element().toString() + " left Queue " + this.getName());
                    }
                    clients.poll();
                    servedClients++;
                }
                synchronized (System.out) {
                    System.out.print(this.getName() + " : ");
                    simArea.append("Queue " + this.getName() + " : ");
                    for(Client c : clients){
                        if(c.getServiceTime() != 0){
                            System.out.print(c);
                            simArea.append(c.toString());
                        }
                    }
                    System.out.println();
                    simArea.append("\n");
                }
            }
            else{
                synchronized (System.out) {
                    if(time > 1){
                        System.out.print(this.getName() + " : \n");
                        simArea.append("Queue " + this.getName() + " : \n");
                    }

                }
            }
            if(!clients.isEmpty() && (time != prevTime)){
                serveClient();
                if(waitingTime.get() > 0)
                    waitingTime.decrementAndGet();
            }

            prevTime = time;
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
