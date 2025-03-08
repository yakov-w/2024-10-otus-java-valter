package ru.otus.crm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("phone")
@NoArgsConstructor
public class Phone implements Cloneable {
    @Id private Long id;
    private String number;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Phone clone() {
        return new Phone(this.id, this.number);
    }
}
