package ru.otus.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "phone_data_set")
public class PhoneDataSet {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    private Client client;

    public PhoneDataSet(){
    }

    public PhoneDataSet(Long id, String number){
        this.id = id;
        this.number = number;
    }

    public PhoneDataSet(String number) {
        this.number = number;
    }

    public PhoneDataSet(String number, Client client) {
        this.number = number;
        this.client = client;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient(){
        return client;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
