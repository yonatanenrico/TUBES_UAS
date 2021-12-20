package com.example.uts.api;

import com.example.uts.entity.Menu;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuResponse {
    private String message;
    @SerializedName("data")
    private List<Menu> listMenu;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Menu> getListMenu() {
        return listMenu;
    }

    public void setListMenu(List<Menu> listMenu) {
        this.listMenu = listMenu;
    }
}
