import java.util.concurrent.Semaphore;

public class Car extends Thread {
    private int id;
    private int gateNumber;
    private int arrivalTime;
    private int parkingDuration;
    IParkingService parkingService;

    Car(int id, int gateNumber, int arrivalTime, int parkingDuration, IParkingService parkingService) {
        this.id = id;
        this.gateNumber = gateNumber;
        this.arrivalTime = arrivalTime;
        this.parkingDuration = parkingDuration;
        this.parkingService = parkingService;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(arrivalTime * 1000);
            System.out.println("Car " + id + " from Gate " + gateNumber + " arrived at time " + arrivalTime);

            // to calc the units of time the car spend waiting
            long startTime = System.currentTimeMillis();

            if (parkingService.park()) {
                System.out.println("Car " + id + " from Gate " + gateNumber + " parked. (Parking Status: " +
                        (4 - parkingService.getAvailableSpots()) + " spots occupied)");
                Thread.sleep(parkingDuration * 1000);

                System.out.println("Car " + id + " from Gate " + gateNumber + " left after " + parkingDuration + " units of time. (Parking Status: " +
                        (4 - parkingService.getAvailableSpots() - 1) + " spots occupied)");
                parkingService.leave();
            } else {
                System.out.println("Car " + id + " from Gate " + gateNumber + " waiting for a spot.");
                parkingService.waitForSpot();

                long endTime = System.currentTimeMillis();
                long waitingTime = (endTime - startTime) / 1000;
                System.out.println("Car " + id + " from Gate " + gateNumber + " parked after waiting for " + waitingTime + " units of time. (Parking Status: " +
                        (4 - parkingService.getAvailableSpots()) + " spots occupied)");

                Thread.sleep(parkingDuration * 1000);
                System.out.println("Car " + id + " from Gate " + gateNumber + " left after " + parkingDuration + " units of time. (Parking Status: " +
                        (4 - parkingService.getAvailableSpots() - 1) + " spots occupied)");
                parkingService.leave();
            }
        } catch (InterruptedException e) {
            System.err.println("Car " + id + " interrupted.");
        }
    }
}
