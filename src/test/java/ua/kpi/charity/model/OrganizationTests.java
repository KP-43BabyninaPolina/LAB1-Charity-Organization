package ua.kpi.charity.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrganizationTests {

    private Organization org;
    private Event event1;
    private Event event2;

    @BeforeEach
    void setUp() {
        org = new Organization("Повернись живим");
        event1 = new Event("Збір на дрони", 50000.0);
        event2 = new Event("Ремонт медичних авто", 100000.0);
    }

    @Test
    void testAddEventSuccess() {
        String result = org.addEvent(event1);

        assertEquals("Захід успішно додано!", result);
        assertEquals(1, org.getEvents().size());
        assertTrue(org.getEvents().contains(event1));
    }

    @Test
    void testAddEventNullThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> org.addEvent(null));
        assertEquals("Захід не може бути null.", exception.getMessage());
    }

    @Test
    void testAddDuplicateEvent() {
        org.addEvent(event1);

        String result = org.addEvent(event1);

        assertEquals("Такий захід вже існує в цій організації.", result);
        assertEquals(1, org.getEvents().size());
    }

    @Test
    void testGetTotalCollectedFunds() {
        org.addEvent(event1);
        org.addEvent(event2);

        assertEquals(0.0, org.getTotalCollectedFunds());

        Donor donor = new Donor("Іван", 200000.0);
        donor.donate(event1, 10000.0, org);
        donor.donate(event2, 25000.0, org);

        assertEquals(35000.0, org.getTotalCollectedFunds());
    }
}