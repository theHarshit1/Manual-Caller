package com.example.manualcaller;

public class CustomerDetails {
    private String name;
    private String number;
    private String email;
    public String key;

    public CustomerDetails() {
    }

    public CustomerDetails(String name, String number,String email) {
        this.name = name;
        this.number = number;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail(){
        return email;
    }
}
