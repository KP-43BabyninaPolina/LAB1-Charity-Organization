package ua.kpi.charity.model;

import java.io.*;
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


    public void exportEvents(String filepath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))) {
            exportEvents(oos);
        }
    }

    public void exportEvents(ObjectOutputStream oos) throws IOException {
        oos.writeObject(this.events);
    }

    public String importEvents(String filepath, boolean overwrite) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))) {
            return importEvents(ois, overwrite);
        }
    }

    @SuppressWarnings("unchecked")
    public String importEvents(ObjectInputStream ois, boolean overwrite) throws IOException, ClassNotFoundException {
        int addedCount = 0;
        int updatedCount = 0;
        var importedEvents = (LinkedList<Event>) ois.readObject();

        for (Event importedEvent : importedEvents) {
            if (this.events.contains(importedEvent)) {
                if (overwrite) {
                    this.events.remove(importedEvent);
                    this.events.add(importedEvent);
                    updatedCount++;
                }
            } else {
                this.events.add(importedEvent);
                addedCount++;
            }
        }
        return String.format("Імпорт завершено. Додано нових заходів: %d, Перезаписано існуючих: %d",
                addedCount, updatedCount);
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
