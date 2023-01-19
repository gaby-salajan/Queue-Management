package GUI;

import Logic.StrategyPolicy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class GUI extends JFrame{
    private JTabbedPane tabPane;
    private JPanel simulationPanel;
    private JPanel settingsPanel;
    private JTextField minArrivalTime;
    private JTextField maxArrivalTime;
    private JTextField minServiceTime;
    private JTextField maxServiceTime;
    private JTextField queuesNo;
    private JTextField simtime;
    private JLabel clientL;
    private JLabel queueL;
    private JLabel simulationL;
    private JLabel minArvTimeL;
    private JLabel maxArvTimeL;
    private JLabel minServTimeL;
    private JLabel maxServTimeL;
    private JLabel queueNoL;
    private JLabel simtimeL;
    private JButton confirmButton;
    private JProgressBar progressBar;
    private JLabel simTimeL;
    private JLabel simTime;
    private JLabel clientsNoL;
    private JTextField clientsNo;
    private JButton startButton;
    private JTextField fileName;
    private JLabel fileNameL;
    private JLabel logL;
    private JLabel strategyL;
    private JLabel strategySelL;
    private JRadioButton queueStrategy;
    private JRadioButton timeStrategy;
    private JTextArea simArea;
    private JScrollPane scrollPane;
    private final AdjustmentListener autoScroll;

    public GUI() {
        super("Assignment 2");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(1000, 700));
        this.setContentPane(tabPane);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        progressBar.setMinimum(0);
        autoScroll = e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum());

        addAllListeners();
    }

    public void refresh(){
        this.repaint();
        this.validate();
    }

    public boolean checkFields(){
        for(Component tf : settingsPanel.getComponents()){
            if(tf.getClass().equals(JTextField.class)){
                if(((JTextField) tf).getText().equals("")){
                    JOptionPane.showMessageDialog(this, "Do not leave empty fields");
                    return false;
                }
            }
        }
        return true;
    }
    public void toSimTab(){
        tabPane.setSelectedIndex(1);
    }
    public boolean generateLog(){
        try {
            File logFile = new File("logs/" + fileName.getText() +".txt");
            if (logFile.createNewFile()) {
                System.out.println("File created: " + logFile.getName());

                PrintStream stream = new PrintStream(logFile);
                System.setOut(stream);
            }
            else {
                System.err.println("File already exists.");
                JOptionPane.showMessageDialog(this, "Please choose other file name");
                return false;
            }
        }catch (IOException e) {
            System.out.println("An error occurred while generating log file.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void addAllListeners(){
        addSettingsListener(clientsNo);
        addSettingsListener(minArrivalTime);
        addSettingsListener(maxArrivalTime);
        addSettingsListener(minServiceTime);
        addSettingsListener(maxServiceTime);
        addSettingsListener(queuesNo);
        addSettingsListener(simtime);
        addSettingsListener(fileName);
        addStrategyButtonsListeners();
        scrollPane.getVerticalScrollBar().addAdjustmentListener(autoScroll);
    }
    private void addSettingsListener(JTextField tf) {
        tf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                tf.setText("");
            }
        });
    }
    public void addConfirmSettingsListener(ActionListener sm_actionListener) {
        confirmButton.addActionListener(sm_actionListener);
    }
    public void addStartButtonListener(ActionListener sm_actionListener) {
        startButton.addActionListener(sm_actionListener);
    }
    public void addStrategyButtonsListeners() {
        timeStrategy.addActionListener(e -> {
            if(timeStrategy.isSelected()){
                queueStrategy.setSelected(false);
            }
        });
        queueStrategy.addActionListener(e -> {
            if(queueStrategy.isSelected()){
                timeStrategy.setSelected(false);
            }
        });
    }
    public void setFinished(){
        scrollPane.getVerticalScrollBar().removeAdjustmentListener(autoScroll);
    }

    public void setSimTime(int simTime) {
        int minutes = simTime / 60;
        int seconds = (simTime - (minutes * 60)) % 60;

        String time = "";
        if(minutes > 9)
            time += ("" + minutes);
        else
            time += ("0" + minutes);
        time += ":";
        if(seconds > 9)
            time += seconds;
        else
            time += ("0" + seconds);

        this.simTime.setText(time);
        this.repaint();
        this.validate();
    }
    public void setProgressBar(int max){
        progressBar.setMaximum(max);
    }
    public void setProgress(int progress){
        progressBar.setValue(progress);
    }

    public int getQueuesNo() {
        return Integer.parseInt(queuesNo.getText());
    }
    public int getSimtime() {
        return Integer.parseInt(simtime.getText());
    }
    public int getClientsNo() {
        return Integer.parseInt(clientsNo.getText());
    }
    public int getMinServiceTime() {
        return Integer.parseInt(minServiceTime.getText());
    }
    public int getMaxServiceTime() {
        return Integer.parseInt(maxServiceTime.getText());
    }
    public int getMinArrivalTime() {
        return Integer.parseInt(minArrivalTime.getText());
    }
    public int getMaxArrivalTime() {
        return Integer.parseInt(maxArrivalTime.getText());
    }
    public JTextArea getSimArea(){
        return simArea;
    }
    public StrategyPolicy getStrategy(){
        if(timeStrategy.isSelected())
            return StrategyPolicy.SHORTEST_TIME;
        if(queueStrategy.isSelected())
            return StrategyPolicy.SHORTEST_QUEUE;
        return null;
    }

    public void showFinishedDialog(){
        JOptionPane.showMessageDialog(this, "Simulation finished.");
    }
    public void showSettingsErrorDialog(){
        JOptionPane.showMessageDialog(this, "Please go to settings and set the app data");
    }
}
