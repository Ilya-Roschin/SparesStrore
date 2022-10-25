package com.example.sparesstrore.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "cars")
public class Car {

    public Car(String carName, String brand) {
        this.carName = carName;
        this.brand = brand;
        this.carUrl = "no data";
    }

    public Car(String carName, String brand, String carUrl) {
        this.carName = carName;
        this.brand = brand;
        this.carUrl = carUrl;
    }

    public Car() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long carId;

    @Column(name = "name")
    private String carName;

    @Column(name = "brand")
    private String brand;

    @Column(name = "url")
    private String carUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    private List<Spare> spares = new ArrayList<>();
}
