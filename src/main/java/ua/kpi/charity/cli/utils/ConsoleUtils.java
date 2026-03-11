package ua.kpi.charity.cli.utils;

import ua.kpi.charity.model.Event;
import ua.kpi.charity.model.Organization;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUtils {

    public static int readInt(Scanner scanner) {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Помилка: Введіть ціле число.");
                scanner.nextLine();
            }
        }
    }

    public static double readDouble(Scanner scanner) {
        while (true) {
            try {
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Помилка: Введіть число (якщо дробове, спробуйте через кому або крапку).");
                scanner.nextLine();
            }
        }
    }

    public static void printEventsList(Organization org) {
        List<Event> events = org.getEvents();
        if (events.isEmpty()) {
            System.out.println("В організації ще немає жодного заходу.");
            return;
        }
        System.out.println("\nСписок заходів організації '" + org.getName() + "':");
        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);
            System.out.printf("%d. [%s] %s (Зібрано: %.2f / %.2f грн)\n",
                    (i + 1), e.getStatus(), e.getName(), e.getCollectedSum(), e.getTargetSum());
        }
    }

    public static Event selectEvent(Organization org, Scanner scanner) {
        if (org.getEvents().isEmpty()) {
            System.out.println("Немає доступних заходів.");
            return null;
        }

        printEventsList(org);
        System.out.print("Оберіть номер заходу: ");
        int index = readInt(scanner) - 1;

        if (index >= 0 && index < org.getEvents().size()) {
            return org.getEvents().get(index);
        } else {
            System.out.println("Невірний номер заходу.");
            return null;
        }
    }
}