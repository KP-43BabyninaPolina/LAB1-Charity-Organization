package ua.kpi.charity.model;

import ua.kpi.charity.model.utils.EventStatus;

import java.io.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;

public class Event implements Serializable /*implements Comparable*/ {
    private final String name;
    private final LinkedList<Donation> donations;
    private EventStatus status;
    private double targetSum;
    private double collectedSum;

    public Event(String name, double targetSum) {
        if (targetSum <= 0) {
            throw new IllegalArgumentException("Цільова сума має бути більшою за 0.");
        }
        this.name = name;
        this.targetSum = targetSum;
        this.status = EventStatus.PLANNED;
        this.collectedSum = 0;
        this.donations = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public EventStatus getStatus() {
        return status;
    }

    public double getTargetSum() {
        return targetSum;
    }

    public void setTargetSum(double targetSum) {
        if (targetSum < collectedSum) {
            throw new IllegalArgumentException("Нова цільова сума не може бути меншою за вже зібрану суму.");
        }
        this.targetSum = targetSum;
    }

    public double getCollectedSum() {
        return collectedSum;
    }

    public LinkedList<Donation> getDonations() {
        return donations;
    }

    public void addDonation(Donation donat) {
        if (status == EventStatus.ACTIVE || status == EventStatus.FINISHED) {
            throw new IllegalStateException("Пожертви не приймаються (захід вже триває або завершено).");
        }

        donations.add(donat);
        collectedSum += donat.getSum();

        if (collectedSum >= targetSum) {
            this.status = EventStatus.SUM_GOAL_REACHED;
        }
    }

    public String start() {
        if (status == EventStatus.SUM_GOAL_REACHED) {
            status = EventStatus.ACTIVE;
            return "Захід успішно розпочато!";
        } else {
            return "Неможливо почати захід: недостатньо зібраних коштів.";
        }
    }

    public String end() {
        if (status == EventStatus.ACTIVE) {
            status = EventStatus.FINISHED;
            return "Захід успішно завершено!";
        } else {
            return "Неможливо завершити захід: його ще не було розпочато.";
        }
    }

    public void exportDonations(String filepath, Comparator<Donation> comparator) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))) {
            exportDonations(oos, comparator);
        }
    }

    public void exportDonations(ObjectOutputStream oos, Comparator<Donation> comparator) throws IOException {
        var listToExport = new LinkedList<>(this.donations);
        if (comparator != null) {
            listToExport.sort(comparator);
        }
        oos.writeObject(listToExport);
    }

    public String importDonations(String filepath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))) {
            return importDonations(ois);
        }
    }

    @SuppressWarnings("unchecked")
    public String importDonations(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        int addedCount = 0;
        var importedDonations = (LinkedList<Donation>) ois.readObject();

        for (Donation d : importedDonations) {
            if (!this.donations.contains(d)) {
                this.donations.add(d);
                this.collectedSum += d.getSum();
                addedCount++;
            }
        }
        if (this.collectedSum >= this.targetSum && this.status == EventStatus.PLANNED) {
            this.status = EventStatus.SUM_GOAL_REACHED;
        }
        return "Імпорт завершено. Додано нових пожертв: " + addedCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name)
                && Double.compare(event.targetSum, targetSum) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, targetSum);
    }
}
