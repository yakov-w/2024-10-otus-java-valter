package ru.otus.crm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "client")
public class Client implements Cloneable {
    @Id
    @Setter
    private Long id;

    private String name;

    @Column("address_id")
    @MappedCollection(idColumn = "client_id")
    private Address address;



    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;

    @SuppressWarnings("this-escape")
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = Set.copyOf(phones);
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        return new Client(
                this.id,
                this.name,
                this.address != null ? this.address.clone() : null,
                this.phones != null ? this.phones.stream().map(Phone::clone).collect(Collectors.toSet()) : null);
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }

}
