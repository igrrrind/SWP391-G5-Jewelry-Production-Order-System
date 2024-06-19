package com.swp391.JewelryProduction.pojos;

import com.swp391.JewelryProduction.util.embeddedID.StaffOrderID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Staff_Order_History")
@IdClass(StaffOrderID.class)
public class StaffOrderHistory {
    @Id
    @Column(name = "staff_id", length = 8, nullable = false, updatable = false, unique = true)
    private String staffID;
    @Id
    @Column(name = "order_id", length = 8, nullable = false, updatable = false, unique = true)
    private String orderID;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;

}
