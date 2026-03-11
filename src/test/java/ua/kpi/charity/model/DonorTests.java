package ua.kpi.charity.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DonorTests {
    private Organization org;
    private Event plannedEvent;
    private Donor donor;

    @BeforeEach
    void setUp() {
        org = new Organization("Повернись живим");

        plannedEvent = new Event("Збір на тепловізори", 50000.0);
        org.addEvent(plannedEvent);

        donor = new Donor("Олександр", 10000.0);
    }

    @Test
    void testDonateSuccess() {
        String result = donor.donate(plannedEvent, 2000.0, org);

        assertTrue(result.startsWith("Пожертву завершено: Дякуємо!"));
        assertEquals(2000.0, plannedEvent.getCollectedSum());
        assertEquals(8000.0, donor.getMoney());
        assertEquals(1, plannedEvent.getDonations().size());
    }

    @Test
    void testDonateLesserThanOne() {
        String result = donor.donate(plannedEvent, 0, org);

        assertEquals("Сума для пожертви має бути більша за 0.", result);
        assertEquals(0.0, plannedEvent.getCollectedSum());
    }

    @Test
    void testDonateMoreThanHave() {
        String result = donor.donate(plannedEvent, 15000.0, org);

        assertEquals("Пожертву скасовано: недостатньо коштів.", result);
        assertEquals(10000.0, donor.getMoney());
        assertEquals(0.0, plannedEvent.getCollectedSum());
    }

    @Test
    void testDonateToWrongEvent() {
        Event wrongEvent = new Event("Невідомий збір", 1000.0);

        String result = donor.donate(wrongEvent, 500.0, org);

        assertEquals("Пожертву скасовано: не знайдено такої події у списку вказаної організації.", result);
        assertEquals(0.0, plannedEvent.getCollectedSum());
        assertEquals(0.0, wrongEvent.getCollectedSum());
        assertEquals(10000.0, donor.getMoney());
    }

    @Test
    void testDonateToEventWithInvalidStatus() {
        Donor donor1 = new Donor("Поліна", 50000.0);
        donor1.donate(plannedEvent, 50000.0, org);
        plannedEvent.start();

        String result = donor.donate(plannedEvent, 1000.0, org);

        assertTrue(result.startsWith("Пожертву скасовано:"));
        assertEquals(50000.0, org.getTotalCollectedFunds());
    }
}
