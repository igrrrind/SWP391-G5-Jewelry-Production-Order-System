package com.swp391.JewelryProduction.dto;

import com.swp391.JewelryProduction.pojos.QuotationItem;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FinalQuotationDTO {
    private String id;
    private String title;
    private LocalDateTime createdDate;
    private LocalDate expiredDate;
    private List<QuotationItem> quotationItems;
}
