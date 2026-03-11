package ua.kpi.charity.cli;

import ua.kpi.charity.cli.utils.ConsoleUtils;
import ua.kpi.charity.model.Donation;
import ua.kpi.charity.model.Event;
import ua.kpi.charity.model.Organization;
import ua.kpi.charity.model.utils.DonationComparator;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class OrganizationMenu {

    public static final String EVENTS_FILEPATH = "events.dat";
    public static final String DONATIONS_FILEPATH = "donations.dat";
    private final Organization org;
    private final Scanner scanner;

    public OrganizationMenu(Organization org, Scanner scanner) {
        this.org = org;
        this.scanner = scanner;
    }

    public void run() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- ПАНЕЛЬ ОРГАНІЗАЦІЇ ---");
            System.out.println("1. Додати новий захід");
            System.out.println("2. Почати / Завершити захід");
            System.out.println("3. Переглянути список заходів");
            System.out.println("4. Переглянути донати конкретного заходу");
            System.out.println("5. Переглянути загальний зібраний фонд організації");
            System.out.println("6. Експорт / Імпорт даних про ЗАХОДИ (Organization)");
            System.out.println("7. Експорт / Імпорт даних про ПОЖЕРТВИ заходу (Event)");
            System.out.println("0. Повернутися до Головного меню");
            System.out.print("Оберіть дію: ");

            int choice = ConsoleUtils.readInt(scanner);
            switch (choice) {
                case 1 -> orgAddEvent();
                case 2 -> orgChangeEventStatus();
                case 3 -> ConsoleUtils.printEventsList(org);
                case 4 -> orgViewEventDonations();
                case 5 -> System.out.printf("Загальний зібраний фонд: %.2f грн\n", org.getTotalCollectedFunds());
                case 6 -> orgIOEvents();
                case 7 -> orgIODonations();
                case 0 -> back = true;
                default -> System.out.println("Невірний вибір.");
            }
        }
    }

    private void orgAddEvent() {
        System.out.print("Введіть назву заходу: ");
        String name = scanner.nextLine();
        System.out.print("Введіть цільову суму (грн): ");
        double target = ConsoleUtils.readDouble(scanner);

        try {
            Event newEvent = new Event(name, target);
            String result = org.addEvent(newEvent);
            System.out.println(result);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private void orgChangeEventStatus() {
        Event event = ConsoleUtils.selectEvent(org, scanner);
        if (event == null) return;

        System.out.println("Поточний статус: " + event.getStatus());
        System.out.println("1. Розпочати захід (перевести в ACTIVE)");
        System.out.println("2. Завершити захід (перевести в FINISHED)");
        System.out.print("Вибір: ");
        int choice = ConsoleUtils.readInt(scanner);

        if (choice == 1) {
            System.out.println(event.start());
        } else if (choice == 2) {
            System.out.println(event.end());
        } else {
            System.out.println("Дію скасовано.");
        }
    }

    private void orgViewEventDonations() {
        Event event = ConsoleUtils.selectEvent(org, scanner);
        if (event == null) return;

        List<Donation> donations = event.getDonations();
        if (donations.isEmpty()) {
            System.out.println("Для цього заходу ще немає пожертв.");
            return;
        }

        System.out.println("Пожертви для '" + event.getName() + "':");
        for (Donation d : donations) {
            System.out.println(" - " + d.getDate()
                    + " | " + d.getSender().getName()
                    + " | " + d.getSum()
                    + " грн | ID: " + d.getTransactionId());
        }
    }

    private void orgIOEvents() {
        System.out.println("1. Експортувати заходи у файл (" + EVENTS_FILEPATH + ")");
        System.out.println("2. Імпортувати заходи з файлу (" + EVENTS_FILEPATH + ")");
        System.out.print("Вибір: ");
        int choice = ConsoleUtils.readInt(scanner);

        try {
            if (choice == 1) {
                org.exportEvents(EVENTS_FILEPATH);
                System.out.println("Заходи успішно експортовано у " + EVENTS_FILEPATH);
            } else if (choice == 2) {
                System.out.print("Перезаписувати існуючі заходи? (1 - Так, 0 - Ні): ");
                boolean overwrite = ConsoleUtils.readInt(scanner) == 1;
                String result = org.importEvents(EVENTS_FILEPATH, overwrite);
                System.out.println(result);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Помилка роботи з файлом: " + e.getMessage());
        }
    }

    private void orgIODonations() {
        Event event = ConsoleUtils.selectEvent(org, scanner);
        if (event == null) return;

        System.out.println("1. Експортувати пожертви у файл (" + DONATIONS_FILEPATH + ")");
        System.out.println("2. Імпортувати пожертви з файлу (" + DONATIONS_FILEPATH + ")");
        System.out.print("Вибір: ");
        int choice = ConsoleUtils.readInt(scanner);

        try {
            if (choice == 1) {
                System.out.println("Як сортувати дані?");
                System.out.println("1. За сумою (зростання) | 2. За сумою (спадання)");
                System.out.println("3. За датою (старіші)   | 4. За датою (новіші)");
                int sortChoice = ConsoleUtils.readInt(scanner);

                var comparator = switch (sortChoice) {
                    case 1 -> DonationComparator.BY_SUM_ASC;
                    case 2 -> DonationComparator.BY_SUM_DESC;
                    case 3 -> DonationComparator.BY_DATE_ASC;
                    case 4 -> DonationComparator.BY_DATE_DESC;
                    default -> null;
                };

                event.exportDonations(DONATIONS_FILEPATH, comparator);
                System.out.println("Пожертви експортовано у " + DONATIONS_FILEPATH);

            } else if (choice == 2) {
                String result = event.importDonations(DONATIONS_FILEPATH);
                System.out.println(result);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Помилка роботи з файлом: " + e.getMessage());
        }
    }
}