package com.swp391.JewelryProduction.pojos;

import com.swp391.JewelryProduction.util.IdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Design {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "design_seq")
    @GenericGenerator(
            name = "design_seq",
            type = IdGenerator.class,
            parameters = {
                    @Parameter(name = IdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "DES"),
                    @Parameter(name = IdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")
            }
    )
    @Column(length = 8, nullable = false, updatable = false, unique = true)
    private String id;
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    @Column(name = "design_link")
    private String designLink;

    @OneToOne(mappedBy = "design")
    private Order order;
}
