package com.example.uts.entity;

import com.google.gson.annotations.SerializedName;

public class Menu {
    @SerializedName("id")
    private long id;
    @SerializedName("nama_menu")
    private String nama_menu;
    @SerializedName("harga")
    private int harga;

    public Menu(String nama_menu, int harga)
    {
        this.nama_menu = nama_menu;
        this.harga = harga;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
}
