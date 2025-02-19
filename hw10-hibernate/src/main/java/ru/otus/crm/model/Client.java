package ru.otus.crm.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) // Возможно не lazy
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client", fetch = FetchType.EAGER)
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @SuppressWarnings("this-escape")
    public <E> Client(Long id, String name, Address address, List<Phone> phones) {
        this(id,name);
        this.address = address;
        this.phones = phones;
        for (Phone phone : this.phones) {
            phone.setClient(this);
        }
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        Client client = new Client(this.id, this.name);
        if (this.address != null) client.setAddress(this.address.clone());
        if (this.phones != null) {
            client.setPhones(this.phones.stream().map(Phone::clone).collect(Collectors.toList()));
            for (Phone phone : client.getPhones()) {
                phone.setClient(client);
            }
        }
        return client;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }

}
