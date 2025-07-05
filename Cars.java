package carsorient;

import java.io.*;
import java.util.*;

class Car {
    String carId;
    String type;
    int seats;
    boolean isSelfDrive;
    double rentalCharge;
    boolean isAvailable;
    int rentedDays;

    Car(String carId, String type, int seats, boolean isSelfDrive, double rentalCharge) {
        this.carId = carId;
        this.type = type;
        this.seats = seats;
        this.isSelfDrive = isSelfDrive;
        this.rentalCharge = rentalCharge;
        this.isAvailable = true;
        this.rentedDays = 0;
    }

    public void displayDetails() {
        System.out.println(carId + " | Type: " + type + " | Seats: " + seats + " | " +
                (isSelfDrive ? "Self Drive" : "With Driver") + " | Rate: ₹" + rentalCharge + "/day" +
                " | Available: " + (isAvailable ? "Yes" : "No"));
    }
}

class Customer {
    String name;
    String idProof;
    String paymentMethod;
    int bookingId;

    Customer(String name, String idProof, String paymentMethod, int bookingId) {
        this.name = name;
        this.idProof = idProof;
        this.paymentMethod = paymentMethod;
        this.bookingId = bookingId;
    }
}

public class Cars {
    static int generateBookingId(Random random) {
        return 100000 + random.nextInt(900000);
    }

    static String collectIdProof(Scanner sc) {
        System.out.print("Enter your ID Proof (Aadhar/License): ");
        return sc.nextLine();
    }

    static void printDriverDetails(Random random, Scanner sc) {
        String[] driverNames = {"Ramesh Kumar", "Suresh Mehta", "Amit Verma", "Karan Patel", "Vikram Singh"};
        int[] driverAges = {35, 40, 29, 33, 38};
        String[] driverPhones = {"9876543210", "8765432109", "9988776655", "8899001122", "7788990011"};

        System.out.println("\nAvailable Drivers:");
        for (int i = 0; i < driverNames.length; i++) {
            System.out.println((i + 1) + ". " + driverNames[i] + " | Age: " + driverAges[i] + " | Phone: " + driverPhones[i]);
        }

        int choice = -1;
        while (choice < 1 || choice > driverNames.length) {
            System.out.print("Select a driver (1-" + driverNames.length + "): ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        int idx = choice - 1;
        System.out.println("\n--- Selected Driver ---");
        System.out.println("Name : " + driverNames[idx]);
        System.out.println("Age  : " + driverAges[idx]);
        System.out.println("Phone: " + driverPhones[idx]);
    }

    static void saveBillToFile(String content) {
        try (FileWriter writer = new FileWriter("rental_log.txt", true)) {
            writer.write(content + "\n-----------------------------------\n");
        } catch (IOException e) {
            System.out.println("Error saving bill: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        List<Car> cars = new ArrayList<>();
        cars.add(new Car("C101", "SUV", 7, true, 3500));
        cars.add(new Car("C102", "Sedan", 5, false, 4000));
        cars.add(new Car("C103", "Hatchback", 4, true, 2500));
        cars.add(new Car("C104", "Luxury", 5, false, 6000));

        while (true) {
            System.out.println("\n===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. View Available Cars");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("\n--- Available Cars ---");
                    for (Car car : cars) {
                        if (car.isAvailable) car.displayDetails();
                    }

                    System.out.print("Enter Car ID to rent: ");
                    String rentId = sc.nextLine();
                    boolean found = false;

                    for (Car car : cars) {
                        if (car.carId.equalsIgnoreCase(rentId) && car.isAvailable) {
                            car.isAvailable = false;
                            found = true;

                            System.out.print("Enter your name: ");
                            String name = sc.nextLine().toUpperCase();

                            String idProof = collectIdProof(sc);
                            System.out.println("ID Proof Received: " + idProof);

                            if (!car.isSelfDrive) printDriverDetails(random, sc);

                            System.out.print("Enter number of days for rent: ");
                            int days = sc.nextInt();
                            sc.nextLine();
                            car.rentedDays = days;

                            System.out.print("Enter payment method (Cash/Card/UPI): ");
                            String payment = sc.nextLine();

                            double totalCharge = car.rentalCharge * days;
                            double gst = totalCharge * 0.18;
                            double grandTotal = totalCharge + gst;
                            int bookingId = generateBookingId(random);

                            Customer customer = new Customer(name, idProof, payment, bookingId);

                            StringBuilder bill = new StringBuilder();
                            bill.append("\n--- Rental Bill ---\n");
                            bill.append("Booking ID     : ").append(customer.bookingId).append("\n");
                            bill.append("Customer Name  : ").append(customer.name).append("\n");
                            bill.append("Car ID         : ").append(car.carId).append("\n");
                            bill.append("Car Type       : ").append(car.type).append("\n");
                            bill.append("Seats          : ").append(car.seats).append("\n");
                            bill.append("Drive Type     : ").append(car.isSelfDrive ? "Self Drive" : "With Driver").append("\n");
                            bill.append("Rental Days    : ").append(days).append("\n");
                            bill.append("Charge/day     : ₹").append(car.rentalCharge).append("\n");
                            bill.append("Rental Charge  : ₹").append(totalCharge).append("\n");
                            bill.append("GST (18%)      : ₹").append(gst).append("\n");
                            bill.append("Total Amount   : ₹").append(grandTotal).append("\n");
                            bill.append("Payment Method : ").append(payment).append("\n");
                            bill.append("Status         : Car rented successfully!");

                            System.out.println(bill);
                            saveBillToFile(bill.toString());
                            break;
                        }
                    }
                    if (!found) System.out.println("Car not found or already rented.");
                }

                case 2 -> {
                    System.out.print("Enter Car ID to return: ");
                    String returnId = sc.nextLine();
                    boolean returned = false;

                    for (Car car : cars) {
                        if (car.carId.equalsIgnoreCase(returnId) && !car.isAvailable) {
                            car.isAvailable = true;
                            returned = true;

                            System.out.print("Enter actual days used: ");
                            int actualDays = sc.nextInt();
                            sc.nextLine();

                            double actualRentCharge = car.rentalCharge * actualDays;
                            double extraCharge = 0;
                            if (actualDays > car.rentedDays) {
                                extraCharge = (actualDays - car.rentedDays) * 500;
                                System.out.println("Extra day charges (₹500/day): ₹" + extraCharge);
                            }

                            double damageCharge = 0;
                            while (true) {
                                System.out.print("Was there any damage? (yes/no): ");
                                String damage = sc.nextLine().trim().toLowerCase();
                                if (damage.equals("yes")) {
                                    while (true) {
                                        System.out.print("Enter damage level (minor/moderate/severe): ");
                                        String level = sc.nextLine().trim().toLowerCase();
                                        switch (level) {
                                            case "minor" -> damageCharge = 500;
                                            case "moderate" -> damageCharge = 1000;
                                            case "severe" -> damageCharge = 2000;
                                            default -> {
                                                System.out.println("Invalid level. Try again.");
                                                continue;
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                } else if (damage.equals("no")) {
                                    break;
                                } else {
                                    System.out.println("Please enter 'yes' or 'no'.");
                                }
                            }

                            double total = actualRentCharge + extraCharge + damageCharge;
                            double gst = total * 0.18;
                            double grandTotal = total + gst;

                            System.out.print("Enter your feedback: ");
                            String feedback = sc.nextLine();

                            if (!car.isSelfDrive) {
                                System.out.print("Enter driver feedback: ");
                                String driverFeedback = sc.nextLine();
                            }

                            StringBuilder returnBill = new StringBuilder();
                            returnBill.append("\n--- Final Bill ---\n");
                            returnBill.append("Car ID         : ").append(car.carId).append("\n");
                            returnBill.append("Car Type       : ").append(car.type).append("\n");
                            returnBill.append("Seats          : ").append(car.seats).append("\n");
                            returnBill.append("Drive Type     : ").append(car.isSelfDrive ? "Self Drive" : "With Driver").append("\n");
                            returnBill.append("Actual Days    : ").append(actualDays).append("\n");
                            returnBill.append("Rent Amount    : ₹").append(actualRentCharge).append("\n");
                            if (extraCharge > 0) returnBill.append("Extra Charges  : ₹").append(extraCharge).append("\n");
                            if (damageCharge > 0) returnBill.append("Damage Charges : ₹").append(damageCharge).append("\n");
                            returnBill.append("GST (18%)      : ₹").append(gst).append("\n");
                            returnBill.append("Total Amount   : ₹").append(grandTotal).append("\n");
                            returnBill.append("Feedback       : ").append(feedback).append("\n");
                            returnBill.append("Status         : Car returned successfully!");

                            System.out.println(returnBill);
                            saveBillToFile(returnBill.toString());
                            break;
                        }
                    }
                    if (!returned) System.out.println("Car ID invalid or not rented.");
                }

                case 3 -> {
                    System.out.println("\n--- Available Cars ---");
                    for (Car car : cars) {
                        if (car.isAvailable) car.displayDetails();
                    }
                }

                case 4 -> {
                    System.out.println("Exiting Car Rental System. Goodbye!");
                    return;
                }

                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}