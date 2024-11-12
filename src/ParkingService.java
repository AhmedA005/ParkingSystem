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
    public int getOccupiedSpots() {
        return 4 - parkingSemaphore.availablePermits();
    }

    @Override
    public int getAvailableSpots() {
        return parkingSemaphore.availablePermits();
    }
}
