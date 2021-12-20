package com.example.uts.rv;

import com.example.uts.entity.Menu;

import java.util.ArrayList;

public class DaftarMenu {
    public ArrayList<Menu> daftarMenu;
    public DaftarMenu(){
        daftarMenu = new ArrayList();
        daftarMenu.add(menu1);
        daftarMenu.add(menu2);
        daftarMenu.add(menu3);
        daftarMenu.add(menu4);
    }

    public static final Menu menu1 = new Menu("Papeda + Ikan kuah kuning", 60000);
    public static final Menu menu2 = new Menu("Ikan Bakar", 35000);
    public static final Menu menu3 = new Menu("Es Sunset", 15000);
    public static final Menu menu4 = new Menu("Es Jeruk", 10000);
}

