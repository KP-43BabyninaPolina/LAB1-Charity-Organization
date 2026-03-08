package ua.kpi.charity.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.kpi.charity.model.utils.EventStatus;

import static org.junit.jupiter.api.Assertions.*;

public class EventTests {
    private Event event;
    private Donor donor;

    @BeforeEach
    void setUp() {
        event = new Event("Медикаменти для шпиталю", 10000.0);
        donor = new Donor("Олена", 50000.0);
    }

    @Test
    void testSetTargetSumLesserThanCollected() {
        Donation donation = new Donation(5000.0, donor, event);
        event.addDonation(donation);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> event.setTargetSum(4000.0));
        assertEquals("Нова цільова сума не може бути меншою за вже зібрану суму.", exception.getMessage());
    }

    @Test
    void testAddDonationAndReachGoal() {
        Donation hugeDonation = new Donation(10000.0, donor, event);
        event.addDonation(hugeDonation);

        assertEquals(10000.0, event.getCollectedSum());
        assertEquals(1, event.getDonations().size());
        assertEquals(EventStatus.SUM_GOAL_REACHED, event.getStatus());
    }

    @Test
    void testStartEventSuccess() {
        event.addDonation(new Donation(10000.0, donor, event));

        String successResult = event.start();

        assertEquals("Захід успішно розпочато!", successResult);
        assertEquals(EventStatus.ACTIVE, event.getStatus());
    }

    @Test
    void testStartEventFail()
    {
        String failResult = event.start();

        assertEquals("Неможливо почати захід: недостатньо зібраних коштів.", failResult);
        assertEquals(EventStatus.PLANNED, event.getStatus());
    }

    @Test
    void testAddDonationWhenEventIsActiveThrowsException() {
        event.addDonation(new Donation(10000.0, donor, event));
        event.start();

        Donation lateDonation = new Donation(500.0, donor, event);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> event.addDonation(lateDonation));
        assertTrue(exception.getMessage().contains("Пожертви не приймаються"));
    }

    @Test
    void testEndEventSuccess() {
        event.addDonation(new Donation(10000.0, donor, event));
        event.start();

        String successResult = event.end();

        assertEquals("Захід успішно завершено!", successResult);
        assertEquals(EventStatus.FINISHED, event.getStatus());
    }

    @Test
    void testEndEventFail()
    {
        String failResult = event.end();

        assertEquals("Неможливо завершити захід: його ще не було розпочато.", failResult);
    }

    @Test
    void testEqualsAndHashCode() {
        Event event1 = new Event("Збір на авто", 15000.0);
        Event event2 = new Event("Збір на авто", 15000.0);

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());

        Event event3 = new Event("Інший збір", 90000.0);

        assertNotEquals(event1, event3);
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    void testConstructorWithInvalidTargetSum() {
        assertThrows(IllegalArgumentException.class, () -> new Event("Поганий збір", 0));

        assertThrows(IllegalArgumentException.class, () -> new Event("Поганий збір 2", -500));
    }
}