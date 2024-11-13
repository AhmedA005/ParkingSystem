import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.Semaphore;

public class ParkingService implements IParkingService {
    private Semaphore parkingSemaphore;

    ParkingService(int parkingSpots) {
        this.parkingSemaphore = new Semaphore(parkingSpots);
    }

    @Override
    public boolean park() {
        return parkingSemaphore.tryAcquire();
    }

    @Override
    public void waitForSpot(Car car) throws InterruptedException {
        try {
            parkingSemaphore.acquire(car);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void leave() throws InterruptedException {
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
