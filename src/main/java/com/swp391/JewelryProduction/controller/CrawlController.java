package com.swp391.JewelryProduction.controller;

import com.swp391.JewelryProduction.dto.DataDTO;
import com.swp391.JewelryProduction.pojos.Material;
import com.swp391.JewelryProduction.services.crawl.ICrawlDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class CrawlController {

    private final ICrawlDataService CrawDataService;

    @PostMapping("crawls")
    public ResponseEntity<DataDTO> crawlingData() {
        try {
            CrawDataService.crawData();
            return ResponseEntity.ok(
                    DataDTO.builder().isSuccess(true).status(HttpStatus.OK.value()).message("Crawl data is successful !")
                            .build());
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataDTO.builder().isSuccess(false).status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Failed to crawl data: " + e.getMessage()).build());
        }
    }

    @GetMapping("crawls")
    public ResponseEntity<List<Material>> readData() throws IOException {
        return ResponseEntity.ok(CrawDataService.getAll());
    }

}
