package ua.kpi.charity.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DonationTests {
    private Donor donor;
    private Event event;
    private Organization org;

    @BeforeEach
    void setUp() {
        donor = new Donor("Марія", 5000.0);
        event = new Event("Збір на аптечки", 20000.0);
        org = new Organization("Допомога безпритульним тваринам");
        org.addEvent(event);
    }

    @Test
    void testGenerateReceipt() {
        var result = donor.donate(event, 1000.0, org);
        var donat = event.getDonations().getFirst();

        var receipt = donat.generateReceipt();

        assertTrue(result.endsWith(receipt));
        assertTrue(receipt.contains("Волонтер Марія пожертвував"));
        assertTrue(receipt.contains("Збір на аптечки"));
        assertTrue(receipt.contains(donat.getTransactionId()));
        assertTrue(receipt.contains("1000"));
    }

    @Test
    void testEqualsAndHashCode() {
        donor.donate(event, 500.0, org);
        donor.donate(event, 500.0, org);

        var don1 = event.getDonations().get(0);
        var don2 = event.getDonations().get(1);

        assertNotEquals(don1, don2);
        assertNotEquals(don1.hashCode(), don2.hashCode());
    }

    @Test
    void testConstructorWithInvalidSum() {
        assertThrows(IllegalArgumentException.class, () -> new Donation(0, donor, event));
        assertThrows(IllegalArgumentException.class, () -> new Donation(-500.0, donor, event));
    }
}
