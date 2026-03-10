package ua.kpi.charity.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.kpi.charity.model.utils.DonationComparator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventExportImportTests {
    private Event event;

    @TempDir
    Path tempDir;

    Path tempFile;

    @Mock
    private Comparator<Donation> mockComparator;

    @BeforeEach
    void setUp() {
        event = new Event("Збір на РЕБ", 100000.0);
        Donor donor = new Donor("Олексій", 50000.0);

        event.processDonation(new Donation(5000.0, donor, event));
        event.processDonation(new Donation(1500.0, donor, event));
        event.processDonation(new Donation(3000.0, donor, event));

        tempFile = tempDir.resolve("test.dat");
    }

    @Test
    void testExportDonationsUsesComparator() throws IOException {
        when(mockComparator.compare(any(Donation.class), any(Donation.class))).thenReturn(0);

        event.exportDonations(tempFile.toString(), mockComparator);

        assertTrue(tempFile.toFile().exists());
        assertTrue(tempFile.toFile().length() > 0);
        verify(mockComparator, atLeastOnce()).compare(any(Donation.class), any(Donation.class));
    }

    @Test
    void testExportAndImportDonationsCycle() throws IOException, ClassNotFoundException {
        event.exportDonations(tempFile.toString(), DonationComparator.BY_SUM_ASC);

        Event newEvent = new Event("Збір на РЕБ (Копія)", 100000.0);
        String importResult = newEvent.importDonations(tempFile.toString());

        assertTrue(importResult.contains("Додано нових пожертв: 3"));
        assertEquals(9500.0, newEvent.getCollectedSum());
        assertEquals(3, newEvent.getDonations().size());
        assertEquals(1500.0, newEvent.getDonations().getFirst().getSum());
    }
}