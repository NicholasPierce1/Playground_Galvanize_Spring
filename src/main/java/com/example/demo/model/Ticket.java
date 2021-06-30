package com.example.demo.model;

public class Ticket{

    public Passenger passenger;

    public double price;

    public double getPrice() {
        return price;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
