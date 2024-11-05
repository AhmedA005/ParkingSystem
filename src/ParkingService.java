import java.util.concurrent.Semaphore;

public class ParkingService implements IParkingService {
    private int parkingSpots;
    private Semaphore parkingSemaphore;

    ParkingService(int parkingSpots, Semaphore parkingSemaphore) {
        this.parkingSpots = parkingSpots;
        this.parkingSemaphore = parkingSemaphore;
    }

    @Override
    public boolean park() {
        return parkingSemaphore.tryAcquire();
    }

    @Override
    public void waitForSpot() throws InterruptedException {
        parkingSemaphore.acquire();
    }

    @Override
    public void leave() {
        parkingSemaphore.release();
    }

    @Override
    public int getOccupiedSpots() {
        return parkingSpots - parkingSemaphore.availablePermits();
    }

    @Override
    public int getAvailableSpots() {
        return parkingSemaphore.availablePermits();
    }
}
