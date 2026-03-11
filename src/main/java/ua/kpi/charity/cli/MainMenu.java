package ua.kpi.charity.cli;

import ua.kpi.charity.cli.utils.ConsoleUtils;
import ua.kpi.charity.model.Donor;
import ua.kpi.charity.model.Organization;

import java.util.Scanner;

public class MainMenu {

    public static void run(Organization org, Donor donor, Scanner scanner) {
        OrganizationMenu orgMenu = new OrganizationMenu(org, scanner);
        DonorMenu donorMenu = new DonorMenu(donor, org, scanner);

        boolean running = true;
        while (running) {
            System.out.println("\n=== БЛАГОДІЙНА ОРГАНІЗАЦІЯ: ГОЛОВНЕ МЕНЮ ===");
            System.out.println("1. Управління організацією (" + org.getName() + ")");
            System.out.println("2. Панель волонтера (" + donor.getName() + ", Баланс: " + donor.getMoney() + " грн)");
            System.out.println("0. Вихід з програми");
            System.out.print("Оберіть роль: ");

            int choice = ConsoleUtils.readInt(scanner);
            switch (choice) {
                case 1 -> orgMenu.run();
                case 2 -> donorMenu.run();
                case 0 -> {
                    System.out.println("Дякуємо за використання програми! Завершуємо сесію...");
                    running = false;
                }
                default -> System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }
}