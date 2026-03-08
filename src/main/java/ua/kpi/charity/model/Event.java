package ua.kpi.charity.model;

import java.util.LinkedList;
import java.util.Objects;

public class Event /*implements Comparable*/ {
    private final String name;
    private EventStatus status;
    private double targetSum;
    private double collectedSum;
    private final LinkedList<Donation> donations;

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

    public void exportDonations() {
    }

    public void importDonations() {
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
