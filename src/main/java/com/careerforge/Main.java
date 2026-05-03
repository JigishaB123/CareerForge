package com.careerforge;

import com.careerforge.portal.CareerPortal;
import com.careerforge.ui.CareerForgeSwingApp;

public class Main {

    public static void main(String[] args) {
        CareerPortal portal = CareerPortal.getInstance();
        portal.loadData();
        Runtime.getRuntime().addShutdownHook(new Thread(portal::saveData));
        CareerForgeSwingApp.open(portal);
    }
}
