package ua.kpi.charity.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Donation implements Serializable {
    private final String transactionId;
    private final Donor sender;
    private final Event event;
    private final double sum;
    private final LocalDateTime date;

    public Donation(double sum, Donor sender, Event event) {
        if (sum < 1) {
            throw new IllegalArgumentException("Сума для пожертви має бути менша за 1.");
        }
        this.transactionId = UUID.randomUUID().toString();
        this.date = LocalDateTime.now();
        this.sum = sum;
        this.sender = sender;
        this.event = event;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Donor getSender() {
        return sender;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getSum() {
        return sum;
    }

    public String generateReceipt() {
        return String.format("Квитанція [%s]: Волонтер %s пожертвував %.2f грн на захід '%s'. Час: %s",
                transactionId,
                sender.getName(),
                sum,
                event.getName(),
                date.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donation donat = (Donation) o;
        return Objects.equals(transactionId, donat.getTransactionId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(transactionId);
    }
}
