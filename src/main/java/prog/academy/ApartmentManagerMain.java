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

                        viewAllApartments(scanner);

                    } else {
                        return;
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            em.close();
            emf.close();
            scanner.close();
        }

    }

    public static void addApartment(Scanner scanner) {
        System.out.println("Input district:");
        String district = scanner.nextLine();

        System.out.println("Input address:");
        String address = scanner.nextLine();

        System.out.println("Input area:");
        float area = scanner.nextFloat();

        System.out.println("Input count of rooms:");
        int rooms = scanner.nextInt();

        System.out.println("Input price:");
        int price = scanner.nextInt();

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

    public static void deleteApartmentById(Scanner scanner) {
        System.out.println("Input apartment id: ");
        long id = scanner.nextLong();

        Apartment apartment = em.getReference(Apartment.class, id);
        if (apartment == null) {
            System.out.println("Apartment was not found!");
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

    public static void changePriceById(Scanner scanner) {
        System.out.println("Input id:");
        long id = scanner.nextLong();

        Apartment apartment = em.getReference(Apartment.class, id);
        if (apartment == null) {
            System.out.println("Apartment was not found!");
            return;
        }

        System.out.println("Input new price:");
        int newPrice = scanner.nextInt();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            apartment.setPrice(newPrice);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
        }

    }

    public static void viewAllApartments(Scanner scanner) {

        Query query = em.createQuery("SELECT x FROM Apartment x", Apartment.class);
        List<Apartment> apartments = query.getResultList();

        for (Apartment apartment : apartments) {
            System.out.println(apartment);
        }

    }

    public static void addRandomApartments(Scanner scanner) {
        System.out.println("Input apartments count:");
        int count = scanner.nextInt();

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

        result.setAddress(streets[random.nextInt(streets.length)]);
        result.setDistrict(districts[random.nextInt(districts.length)]);
        result.setRooms(random.nextInt(1, 6));
        result.setArea(random.nextFloat(30, 120));
        result.setPrice((int) (result.getArea() * priceM2));

        return result;

    }
}
