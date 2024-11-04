import java.io.PrintWriter;
import java.util.concurrent.Semaphore;

public class Car extends Thread {
    private int id;
    private int gateNumber;
    private int arrivalTime;
    private int parkingDuration;
    private Semaphore semaphore;
    private PrintWriter fileWriter;

    Car(int id, int gateNumber, int arrivalTime, int parkingDuration, Semaphore semaphore, PrintWriter fileWriter) {
        this.id = id;
        this.gateNumber = gateNumber;
        this.arrivalTime = arrivalTime;
        this.parkingDuration = parkingDuration;
        this.semaphore = semaphore;
        this.fileWriter = fileWriter;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(arrivalTime * 1000);
            synchronized (fileWriter) {
                fileWriter.println("Car " + id + " from Gate " + gateNumber + " arrived at " + arrivalTime);
            }
//            System.out.println("Car " + id + " from Gate " + gateNumber + " arrived at " + arrivalTime);

            // to calc the units of time the car spend waiting
            long startTime = System.currentTimeMillis();

            if (semaphore.tryAcquire()) {
                synchronized (fileWriter) {
                    fileWriter.println("Car " + id + " from Gate " + gateNumber + " parked. (Parking Status: " +
                            (4 - semaphore.availablePermits()) + " spots occupied)");
                }
//                System.out.println("Car " + id + " from Gate " + gateNumber + " parked. (Parking Status: " +
//                        (4 - semaphore.availablePermits()) + " spots occupied)");
                Thread.sleep(parkingDuration * 1000);

                synchronized (fileWriter) {
                    fileWriter.println("Car " + id + " from Gate " + gateNumber + " left after " + parkingDuration + " units of time. (Parking Status: " +
                            (4 - semaphore.availablePermits() - 1) + " spots occupied)");
                }
//                System.out.println("Car " + id + " from Gate " + gateNumber + " left after " + parkingDuration + " units of time. (Parking Status: " +
//                        (4 - semaphore.availablePermits() - 1) + " spots occupied)");
                semaphore.release();
            } else {
                synchronized (fileWriter) {
                    fileWriter.println("Car " + id + " from Gate " + gateNumber + " waiting for a spot");
                }
//                System.out.println("Car " + id + " from Gate " + gateNumber + " waiting for a spot");
                semaphore.acquire();

                long endTime = System.currentTimeMillis();
                long waitingTime = (endTime - startTime) / 1000;
                synchronized (fileWriter) {
                    fileWriter.println("Car " + id + " from Gate " + gateNumber + " parked after waiting for " + waitingTime + " units of time. (Parking Status: " +
                            (4 - semaphore.availablePermits()) + " spots occupied)");
                }
//                System.out.println("Car " + id + " from Gate " + gateNumber + " parked after waiting for " + waitingTime + " units of time. (Parking Status: " +
//                        (4 - semaphore.availablePermits()) + " spots occupied");

                Thread.sleep(parkingDuration * 1000);
                synchronized (fileWriter) {
                    fileWriter.println("Car " + id + " from Gate " + gateNumber + " left after " + parkingDuration + " units of time. (Parking Status: " +
                            (4 - semaphore.availablePermits() - 1) + " spots occupied)");
                }
//                System.out.println("Car " + id + " from Gate " + gateNumber + " left after " + parkingDuration + " units of time. (Parking Status: " +
//                        (4 - semaphore.availablePermits() - 1) + " spots occupied)");
                semaphore.release();
            }
        } catch (InterruptedException e) {
            synchronized (fileWriter) {
                fileWriter.println("Car " + id + " interrupted.");
            }
//            System.err.println("Car " + id + " interrupted.");
        }
    }
}
