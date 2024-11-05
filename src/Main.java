import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        int parkingSpots = 4;
        Semaphore semaphore = new Semaphore(parkingSpots, true);
        ParkingService parkingService = new ParkingService(parkingSpots, semaphore);

        Gate gate1 = new Gate(1, parkingService);
        Gate gate2 = new Gate(2, parkingService);
        Gate gate3 = new Gate(3, parkingService);

        List<Car> cars = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                int gateNumber = Integer.parseInt(parts[0].substring(5).trim());
                int carId = Integer.parseInt(parts[1].substring(4).trim());
                int arrivalTime = Integer.parseInt(parts[2].substring(7).trim());
                int parkingDuration = Integer.parseInt(parts[3].substring(6).trim());

                Car car = new Car(carId, gateNumber, arrivalTime, parkingDuration, parkingService);
                cars.add(car);

                switch (gateNumber) {
                    case 1:
                        gate1.serveCar(car);
                        break;
                    case 2:
                        gate2.serveCar(car);
                        break;
                    case 3:
                        gate3.serveCar(car);
                        break;
                    default:
                        System.out.println("Invalid gate number: " + gateNumber);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Car car : cars) {
            try {
                car.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Total Cars Served: " +
                (gate1.getCarsServed() + gate2.getCarsServed() + gate3.getCarsServed()));
        System.out.println("Current Cars in Parking: " + Gate.getCarsCurrentlyParked());
        System.out.println("Details:");
        gate1.reportStatus();
        gate2.reportStatus();
        gate3.reportStatus();
    }
}