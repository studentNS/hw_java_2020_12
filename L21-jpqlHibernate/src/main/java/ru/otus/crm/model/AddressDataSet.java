package ru.otus.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "address_data_set")
public class AddressDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private Client client;

    public AddressDataSet() {
    }

    public AddressDataSet(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public AddressDataSet(String street) {
        this.street = street;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
