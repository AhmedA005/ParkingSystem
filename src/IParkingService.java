public interface IParkingService {
    boolean park();
    void waitForSpot() throws InterruptedException;
    void leave();
    int getOccupiedSpots();
    int getAvailableSpots();
}
