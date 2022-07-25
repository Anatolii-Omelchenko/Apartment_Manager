package prog.academy;


import javax.persistence.*;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ApartmentManagerMain {

    static EntityManager em;
    static EntityManagerFactory emf;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            emf = Persistence.createEntityManagerFactory("Apartment_DB");
            em = emf.createEntityManager();

            try {
                while (true) {
                    System.out.println("1: add apartment");
                    System.out.println("2: add random apartments");
                    System.out.println("3: delete apartment");
                    System.out.println("4: change apartment price");
                    System.out.println("5: view all apartments");
                    System.out.print("-> ");

                    String choose = scanner.nextLine();

                    if (choose.equals("1")) {
                        addApartment(scanner);
                    } else if (choose.equals("2")) {
                        addRandomApartments(scanner);
                    } else if (choose.equals("3")) {
                        deleteApartmentById(scanner);
                    } else if (choose.equals("4")) {
                        changePriceById(scanner);
                    } else if (choose.equals("5")) {
                        viewAllApartments();
                    } else {
                        return;
                    }
                }
            } finally {
                scanner.close();
                em.close();
                emf.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

    }

    private static void addApartment(Scanner scanner) {
        System.out.println("Input district:");
        String district = scanner.nextLine();

        System.out.println("Input address:");
        String address = scanner.nextLine();

        float area = 0;
        int rooms = 0;
        int price = 0;

        while (true) {
            try {
                System.out.println("Input area:");
                area = Float.parseFloat(scanner.nextLine());

                System.out.println("Input count of rooms:");
                rooms = Integer.parseInt(scanner.nextLine());

                System.out.println("Input price:");
                price = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Wrong input format!");
                continue;
            }
        }

        Apartment apartment = new Apartment(district, address, area, rooms, price);

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            em.persist(apartment);
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
        }

    }

    private static void deleteApartmentById(Scanner scanner) {
        System.out.println("Input apartment id: ");
        long id = Long.parseLong(scanner.nextLine());

        Apartment apartment = em.find(Apartment.class, id);
        if (apartment == null) {
            System.out.println("Apartment with id " + id + " was not found!");
            return;
        }

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            em.remove(apartment);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
        }

    }

    private static void changePriceById(Scanner scanner) {
        System.out.println("Input id:");
        long id = Long.parseLong(scanner.nextLine());

        Apartment apartment = em.find(Apartment.class, id);
        if (apartment == null) {
            System.out.println("Apartment with id " + id + " was not found!");
            return;
        }

        System.out.println("Input new price:");
        int newPrice = Integer.parseInt(scanner.nextLine());

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            apartment.setPrice(newPrice);
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
        }

    }

    private static void viewAllApartments() {

        Query query = em.createQuery("SELECT x FROM Apartment x", Apartment.class);
        List<Apartment> apartments = query.getResultList();

        for (Apartment apartment : apartments) {
            System.out.println(apartment);
        }

    }

    private static void addRandomApartments(Scanner scanner) {
        System.out.println("Input apartments count:");
        int count = Integer.parseInt(scanner.nextLine());

        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        try {
            for (int i = 0; i < count; i++) {
                Apartment apartment = getRandomApartment();
                em.persist(apartment);
            }
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
        }
    }

    private static Apartment getRandomApartment() {

        Apartment result = new Apartment();

        final Random random = new Random();
        final int priceM2 = 9000;
        final String[] districts = {"Shevchenko district", "Southern district", "Kyiv district"};
        final String[] streets = {"Shevchenko", "Green", "Private", "European", "Hohol", "Central"};

        String address = streets[random.nextInt(streets.length)] + ", " + random.nextInt(198);

        result.setAddress(address);
        result.setDistrict(districts[random.nextInt(districts.length)]);
        result.setRooms(random.nextInt(1, 6));
        result.setArea(random.nextFloat(30, 120));
        result.setPrice((int) (result.getArea() * priceM2));

        return result;

    }
}
