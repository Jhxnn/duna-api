package com.dune.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "dunes")
public class Dune {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID duneId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(referencedColumnName = "id", name = "owner_id")
    private User owner;

    private int totalPoints;


    public UUID getDuneId() {
        return duneId;
    }

    public void setDuneId(UUID duneId) {
        this.duneId = duneId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
}
