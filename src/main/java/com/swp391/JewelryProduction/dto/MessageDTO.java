package com.swp391.JewelryProduction.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MessageDTO {
    private UUID id;
}
