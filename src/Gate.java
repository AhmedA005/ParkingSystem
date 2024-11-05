import java.io.PrintWriter;
import java.util.concurrent.Semaphore;

public class Gate {
    private int gateNumber;
    private int carsServed;
    private static int carsCurrentlyParked = 0;
    private IParkingService parkingService;

    public Gate(int gateNumber, IParkingService parkingService) {
        this.gateNumber = gateNumber;
        this.carsServed = 0;
        this.parkingService = parkingService;
    }

    public synchronized void incrementCarsServed() {
        carsServed++;
    }

    public static synchronized void incrementCarsCurrentlyParked() {
        carsCurrentlyParked++;
    }

    public static synchronized void decrementCarsCurrentlyParked() {
        carsCurrentlyParked--;
    }

    public void serveCar(Car car) {
        incrementCarsServed();
        car.start();
    }

    public static int getCarsCurrentlyParked() {
        return carsCurrentlyParked;
    }

    public int getCarsServed() {
        return carsServed;
    }

    public void reportStatus() {
        System.out.println(" - Gate " + gateNumber + " served " + carsServed + " cars.");
    }
}
