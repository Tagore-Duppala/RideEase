package com.project.rideEase.entities;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_vehicleId", columnList = "vehicleId")
})
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double rating;


    private Boolean available;

    private String vehicleId;

    @Column(columnDefinition = "Geometry(Point, 4326)")
    private Point currentLocation;
}
