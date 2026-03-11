package ua.kpi.charity.cli;

import ua.kpi.charity.cli.utils.ConsoleUtils;
import ua.kpi.charity.model.Donation;
import ua.kpi.charity.model.Donor;
import ua.kpi.charity.model.Event;
import ua.kpi.charity.model.Organization;

import java.util.Scanner;

public class DonorMenu {

    private final Donor donor;
    private final Organization org;
    private final Scanner scanner;

    public DonorMenu(Donor donor, Organization org, Scanner scanner) {
        this.donor = donor;
        this.org = org;
        this.scanner = scanner;
    }

    public void run() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- ПАНЕЛЬ ВОЛОНТЕРА ---");
            System.out.println("Волонтер: " + donor.getName() + " | Доступно коштів: " + donor.getMoney() + " грн");
            System.out.println("1. Переглянути список заходів");
            System.out.println("2. Зробити пожертву");
            System.out.println("3. Поповнити свій рахунок");
            System.out.println("4. Отримати квитанції пожертв");
            System.out.println("0. Повернутися до Головного меню");
            System.out.print("Оберіть дію: ");

            int choice = ConsoleUtils.readInt(scanner);
            switch (choice) {
                case 1 -> ConsoleUtils.printEventsList(org);
                case 2 -> donorMakeDonation();
                case 3 -> donorAddMoney();
                case 4 -> printDonorReceipts();
                case 0 -> back = true;
                default -> System.out.println("Невірний вибір.");
            }
        }
    }

    private void donorMakeDonation() {
        Event event = ConsoleUtils.selectEvent(org, scanner);
        if (event == null) return;

        System.out.print("Введіть суму пожертви (грн): ");
        double sum = ConsoleUtils.readDouble(scanner);

        String result = donor.donate(event, sum, org);
        System.out.println(result);
    }

    private void donorAddMoney() {
        System.out.print("На скільки гривень поповнити рахунок? ");
        double sum = ConsoleUtils.readDouble(scanner);
        if (sum > 0) {
            donor.setMoney(donor.getMoney() + sum);
            System.out.println("Рахунок поповнено! Поточний баланс: " + donor.getMoney() + " грн.");
        } else {
            System.out.println("Сума поповнення має бути більшою за 0.");
        }
    }


    private void printDonorReceipts() {
        System.out.println("\n--- ВАШІ КВИТАНЦІЇ ---");
        boolean found = false;

        for (Event event : org.getEvents()) {
            for (Donation donation : event.getDonations()) {
                if (donation.getSender().equals(donor)) {
                    System.out.println(donation.generateReceipt());
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("У вас ще немає жодної квитанції про пожертву.");
        }
        System.out.println("----------------------");
    }
}
