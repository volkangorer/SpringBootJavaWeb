package com.example.JavaWeb.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Userbook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    int id;
    String bookname;
    String username;

    int is_reserved;

    String reservationDate;

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getIs_reserved() {
        return is_reserved;
    }

    public void setIs_reserved(int is_reserved) {
        this.is_reserved = is_reserved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}