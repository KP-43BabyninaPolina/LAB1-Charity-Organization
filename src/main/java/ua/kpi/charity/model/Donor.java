package ua.kpi.charity.model;

import java.io.Serializable;
import java.util.Objects;

public class Donor implements Serializable {

    private final String name;
    private double money;

    public Donor(String name, double money) {
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        if (money < 0) {
            throw new IllegalArgumentException("Нова сума грошей не може бути меншою за 0.");
        }

        this.money = money;
    }

    public String donate(Event event, double sum, Organization org) {
        if (event == null || org == null) {
            throw new IllegalArgumentException();
        }

        if (sum < 1) {
            return "Сума для пожертви має бути більша за 0.";
        }

        var eventList = org.getEvents();
        if (!eventList.contains(event)) {
            return "Пожертву скасовано: не знайдено такої події у списку вказаної організації.";
        }

        if (sum > money) {
            return "Пожертву скасовано: недостатньо коштів.";
        }

        try {
            money -= sum;
            var donat = new Donation(sum, this, event);
            event.processDonation(donat);
            return "Пожертву завершено: Дякуємо!\n" + donat.generateReceipt();
        } catch (IllegalStateException e) {
            money += sum;
            return "Пожертву скасовано: " + e.getMessage();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donor donor = (Donor) o;
        return Objects.equals(name, donor.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}

