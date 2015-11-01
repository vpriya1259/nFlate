package com.nflate;

/**
 * Created by vishnu on 10/31/2015.
 */
public class Settings {
  private Setting[] cart;

  private Setting[] home;

  public Setting[] getCart() {
    return cart;
  }

  public void setCart(Setting[] cart) {
    this.cart = cart;
  }

  public Setting[] getHome() {
    return home;
  }

  public void setHome(Setting[] home) {
    this.home = home;
  }

  public static class Setting {

    private String name;
    private String theme;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getTheme() {
      return theme;
    }

    public void setTheme(String theme) {
      this.theme = theme;
    }
  }

}
