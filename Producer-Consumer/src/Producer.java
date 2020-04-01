import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Producer extends Thread {
    private static final int MAX_SIZE = 5;
    private final List<String> messages = new ArrayList<>();

    @Override
    public void run() {
        try {
            while(true) {
                produce();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    // only one thread will get access to the method at a time
    private synchronized void produce() throws Exception{
        while(messages.size() == MAX_SIZE) {
            wait();
        }
        String data = LocalDateTime.now().toString();
        messages.add(data);
        System.out.println("Producer produced data.");
        notify();
    }

    public synchronized String consume() throws Exception {
        notify();
        while(messages.isEmpty()) {
            wait();
        }
        String data = messages.get(0);
        messages.remove(data);
        return data;
    }
}