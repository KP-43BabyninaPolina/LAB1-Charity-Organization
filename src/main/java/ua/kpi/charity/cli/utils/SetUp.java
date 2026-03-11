package ua.kpi.charity.cli.utils;

import ua.kpi.charity.model.Donor;
import ua.kpi.charity.model.Event;
import ua.kpi.charity.model.Organization;

public class SetUp {

    public static Organization initOrganization() {
        Organization org = new Organization("Фонд 'Повернись живим'");

        Event demoEvent = new Event("Збір на FPV-дрони", 50000.0);
        org.addEvent(demoEvent);

        var demoDonor = new Donor("Поліна", 50000.0);
        demoDonor.donate(demoEvent, 50000.0, org);

        org.addEvent(new Event("Медикаменти для шпиталю", 15000.0));

        return org;
    }

    public static Donor initDonor() {
        return new Donor("Марія", 20000.0);
    }
}
