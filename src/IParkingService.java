public interface IParkingService {
    boolean park();
    void waitForSpot(Car car) throws InterruptedException;
    void leave();
    int getOccupiedSpots();
    int getAvailableSpots();
}
