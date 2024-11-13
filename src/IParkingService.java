public interface IParkingService {
    boolean park();
    void waitForSpot(Car car) throws InterruptedException;
    void leave() throws InterruptedException;
    int getOccupiedSpots();
    int getAvailableSpots();
}
