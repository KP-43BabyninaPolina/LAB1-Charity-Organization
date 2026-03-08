package ua.kpi.charity.model;

import java.util.LinkedList;
import java.util.Objects;

public class Organization {
    private String name;
    private LinkedList<Event> events;

    public Organization(String name) {
        this.name = name;
        events = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Event> getEvents() {
        return events;
    }

    public void setEvents(LinkedList<Event> events) {
        this.events = events;
    }

    public String addEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Захід не може бути null.");
        }

        if (events.contains(event)) {
            return "Такий захід вже існує в цій організації.";
        }

        events.add(event);
        return "Захід успішно додано!";
    }

    public double getTotalCollectedFunds() {
        double total = 0;
        for (Event event : events) {
            total += event.getCollectedSum();
        }
        return total;
    }

    public void exportEvents() {
    }

    public void importEvents() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization organization = (Organization) o;
        return Objects.equals(name, organization.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
