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
@Table(name = "spares")
public class Spare {

    public Spare(String spareName, int price) {
        this.spareName = spareName;
        this.price = price;
        this.spareUrl = "no data";
    }

    public Spare() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long spareId;

    @Column(name = "name")
    private String spareName;

    @Column(name = "price")
    private int price;

    @Column(name = "url")
    private String spareUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "spare")
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categories_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cars_id", nullable = false)
    private Car car;

}
