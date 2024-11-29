package com.andersen.java_intensive_13.hotel_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "apartment", schema = "public")
public class Apartment extends AuditableEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "price")
    private Double price;

    @JsonProperty("isReserved")
    private Boolean isReserved;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = true)
    @JoinColumn(referencedColumnName = "id")
    private User user;
}
