package ua.kpi.charity.model.utils;

import ua.kpi.charity.model.Donation;

import java.util.Comparator;

public class DonationComparator {

    public static final Comparator<Donation> BY_DATE_ASC = Comparator.comparing(Donation::getDate);

    public static final Comparator<Donation> BY_DATE_DESC = Comparator.comparing(Donation::getDate).reversed();

    public static final Comparator<Donation> BY_SUM_ASC = Comparator.comparingDouble(Donation::getSum);

    public static final Comparator<Donation> BY_SUM_DESC = Comparator.comparingDouble(Donation::getSum).reversed();
}

