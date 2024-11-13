import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Semaphore {
    private AtomicInteger availableSpots;
    private BlockingQueue<Car> waitingQueue;

    Semaphore(int availableSpots) {
        this.availableSpots = new AtomicInteger(availableSpots);
        waitingQueue = new LinkedBlockingQueue<>();
    }

    public synchronized boolean tryAcquire() {
        if (availableSpots.get() > 0) {
            availableSpots.decrementAndGet();
            return true;
        }
        return false;
    }

    public synchronized void acquire(Car car) throws InterruptedException {
        if (availableSpots.get() > 0) {
            availableSpots.decrementAndGet();
        } else {
            waitingQueue.put(car);
            while (waitingQueue.peek() != car || availableSpots.get() <= 0) {
                this.wait();
            }
            waitingQueue.poll();
            availableSpots.decrementAndGet();
        }
    }

    public synchronized void release() {
        availableSpots.incrementAndGet();
        notifyAll();
    }

    public int availablePermits() {
        return availableSpots.get();
    }


}
