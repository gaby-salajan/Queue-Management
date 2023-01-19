package Logic;

import GUI.GUI;
import Model.Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class SimManager extends Thread{
    private boolean settingsDone = false;

    private int simulationTime;
    private int queuesNo;
    private int clientsNo;

    private int minServiceTime;
    private int maxServiceTime;
    private int minArrivalTime;
    private int maxArrivalTime;

    private Scheduler scheduler;
    private GUI gui;
    private List<Client> clients;


    public SimManager(){
        super("Queue Manager");
        gui = new GUI();
        gui.addConfirmSettingsListener(new ConfirmButtonListener());
        gui.addStartButtonListener(new StartButtonListener());

        clients = Collections.synchronizedList(new ArrayList<>());
    }

    private void generateClients(){
        for(int i = 1; i <= clientsNo; i++){
            clients.add(new Client(i, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime));
        }
    }


    private class ConfirmButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            settingsDone = gui.checkFields();
            if(settingsDone){
                simulationTime = gui.getSimtime();
                queuesNo = gui.getQueuesNo();
                clientsNo = gui.getClientsNo();
                minServiceTime = gui.getMinServiceTime();
                maxServiceTime = gui.getMaxServiceTime();
                minArrivalTime = gui.getMinArrivalTime();
                maxArrivalTime = gui.getMaxArrivalTime();
                gui.setSimTime(simulationTime);
                gui.setProgressBar(simulationTime);
                gui.setProgress(0);
                gui.refresh();

                settingsDone = gui.generateLog();
            }
            if(settingsDone)
                gui.toSimTab();
        }
    }
    private class StartButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(settingsDone){
                generateClients();

                scheduler = new Scheduler(queuesNo, gui.getSimArea());

                if(gui.getStrategy() != null){
                    StrategyPolicy strategyPolicy = gui.getStrategy();
                    scheduler.setStrategy(strategyPolicy);
                    System.out.println(strategyPolicy);
                }
                else
                    System.err.println("Strategy selection error");

                System.out.println("Simulation started");
                gui.getSimArea().append("Simulation started\n");
                System.out.print("Clients : ");
                gui.getSimArea().append("Clients : ");

                int count = 0;
                for(Client c : clients){
                    count++;
                    if(count == 100){
                        gui.getSimArea().append("\n");
                        count = 0;
                    }
                    System.out.print(c.toString()+" ");
                    gui.getSimArea().append(c + " ");
                }
                System.out.println();
                gui.getSimArea().append("\n");

                start();
                scheduler.startThreads();
            }
            else{
                gui.showSettingsErrorDialog();
            }

        }
    }


    @Override
    public void run() {
        int currentTime = 0;
        int peakTime = 0;
        int queuesTime = 0;

        while (currentTime <= simulationTime){
            currentTime++;
            if(clients.isEmpty() && scheduler.queuesFinished())
                break;

            System.out.println("\nSimTime: " + currentTime);
            gui.getSimArea().append("\nSimTime: " + currentTime);
            gui.getSimArea().append("\n");
            gui.refresh();

            gui.setSimTime((simulationTime-currentTime));
            gui.setProgress(currentTime);
            List<Client> toRemove = new ArrayList<>();
            synchronized (clients){
                for(Client c : clients){
                    if(c.getArrivalTime() == currentTime){
                        scheduler.sendClientToQueue(c);
                        toRemove.add(c);
                    }
                }
                if(queuesTime <= scheduler.getQueuesTime()){
                    queuesTime = scheduler.getQueuesTime();
                    peakTime = currentTime;
                }

                for(Client c : toRemove){
                    clients.remove(c);
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        scheduler.stopThreads();

        System.out.println("\nSimulation finished");
        System.out.println("Avg waiting time: " + scheduler.calculateAvgWaitingTime());
        System.out.println("Peak time: " + peakTime);
        gui.getSimArea().append("\nSimulation finished");
        gui.getSimArea().append("\nAvg waiting time: " + scheduler.calculateAvgWaitingTime());
        gui.getSimArea().append("\nPeak time: " + peakTime);

        gui.showFinishedDialog();
        gui.setFinished();
    }
}
