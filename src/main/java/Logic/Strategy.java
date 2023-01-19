package Logic;

import Model.Client;
import Model.Queue;

import java.util.List;

public interface Strategy {
    void addClientToQueue(List<Queue> queues, Client client);
}
