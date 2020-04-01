public class Main {
    public static void main(String[] args) {
        
        Producer producer = new Producer();
        producer.setName("Producer-1");
        producer.start();

        Consumer consumer = new Consumer(producer);
        consumer.setName("Consumer-1");
        consumer.start();
    }
}