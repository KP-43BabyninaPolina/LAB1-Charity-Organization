package ua.kpi.charity;

import ua.kpi.charity.cli.MainMenu;
import ua.kpi.charity.cli.utils.SetUp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        var org = SetUp.initOrganization();
        var donor = SetUp.initDonor();

        MainMenu.run(org, donor, scanner);

        scanner.close();
    }
}
