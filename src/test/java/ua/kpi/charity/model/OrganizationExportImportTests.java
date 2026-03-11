package ua.kpi.charity.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrganizationExportImportTests {

    private Organization org;
    private Event activeEvent;

    @Mock
    private ObjectOutputStream mockOutStream;

    @Mock
    private ObjectInputStream mockInStream;

    @BeforeEach
    void setUp() {
        org = new Organization("Фонд Притули");

        activeEvent = new Event("Бронежилети", 500000.0);
        org.addEvent(activeEvent);

        Donor donor = new Donor("Анна", 1000000.0);
        activeEvent.processDonation(new Donation(10000.0, donor, activeEvent));
    }

    @Test
    void testExportEvents() throws IOException {
        org.exportEvents(mockOutStream);

        verify(mockOutStream, times(1)).writeObject(any());
    }

    @Test
    void testImportEventsWithOverwriteAppliesChanges() throws IOException, ClassNotFoundException {
        var simulatedFileContent = simulateFileContent();
        when(mockInStream.readObject()).thenReturn(simulatedFileContent);

        activeEvent.processDonation(new Donation(50000.0, new Donor("Тестер", 50000.0), activeEvent));
        assertEquals(60000.0, org.getTotalCollectedFunds());

        String result = org.importEvents(mockInStream, true);

        verify(mockInStream, times(1)).readObject();
        assertTrue(result.contains("Перезаписано"));
        assertEquals(1, org.getEvents().size());
        assertEquals(10000.0, org.getTotalCollectedFunds());
    }

    @Test
    void testImportEventsWithoutOverwriteIgnoresChanges() throws IOException, ClassNotFoundException {
        var simulatedFileContent = simulateFileContent();
        when(mockInStream.readObject()).thenReturn(simulatedFileContent);

        activeEvent.processDonation(new Donation(20000.0, new Donor("Тестер", 20000.0), activeEvent));

        String result = org.importEvents(mockInStream, false);

        verify(mockInStream, times(1)).readObject();
        assertTrue(result.contains("Перезаписано існуючих: 0"));
        assertEquals(30000.0, org.getTotalCollectedFunds());
    }

    private LinkedList<Event> simulateFileContent() {
        var simulatedFileContent = new LinkedList<Event>();
        var oldEvent = new Event("Бронежилети", 500000.0);
        oldEvent.processDonation(new Donation(10000.0, new Donor("Анна", 1000000.0), oldEvent));
        simulatedFileContent.add(oldEvent);

        return simulatedFileContent;
    }
}