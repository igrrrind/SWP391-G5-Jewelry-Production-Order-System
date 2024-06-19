package com.swp391.JewelryProduction.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swp391.JewelryProduction.util.IdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "final_quotation")
public class Quotation {
    @Id
    @GeneratedValue(generator = "quotation_seq")
    @GenericGenerator(
            name = "quotation_seq",
            type = IdGenerator.class,
            parameters = {
                    @Parameter(name = IdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "QUO"),
                    @Parameter(name = IdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")
            }
    )
    @Column(length = 8, nullable = false, unique = true)
    private String id;
    private String title;
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "expired_date", nullable = false)
    private LocalDate expiredDate;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<QuotationItem> quotationItems;

    @OneToOne(mappedBy = "quotation", fetch = FetchType.LAZY)
    private Order order;

    public Double getTotalPrice () {
        double total = 0.0;
        for (var item: quotationItems) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
