import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class ParkingService implements IParkingService {
    private Semaphore parkingSemaphore;
    private BlockingQueue<Car> waitingQueue;

    ParkingService(int parkingSpots) {
        this.parkingSemaphore = new Semaphore(parkingSpots, true);
        this.waitingQueue = new LinkedBlockingQueue<Car>();
    }

    @Override
    public boolean park() {
        if (!waitingQueue.isEmpty()) {
            waitingQueue.offer((Car) Thread.currentThread());
            try {
                // Wait until the current car's turn comes up
                waitingQueue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        // current car's turn has come up, try to acquire a parking spot
        return parkingSemaphore.tryAcquire();
    }

    @Override
    public void waitForSpot(Car car) throws InterruptedException {
        waitingQueue.put(car);
        parkingSemaphore.acquire();
        waitingQueue.take();
    }

    @Override
    public void leave() {
        parkingSemaphore.release();
    }

    @Override
    public synchronized int getOccupiedSpots() {
        return 4 - parkingSemaphore.availablePermits();
    }

    @Override
    public synchronized int getAvailableSpots() {
        return parkingSemaphore.availablePermits();
    }
}
