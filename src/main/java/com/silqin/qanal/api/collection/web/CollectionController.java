package com.silqin.qanal.api.collection.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.silqin.qanal.core.crawler.coupang.CoupangCollector;
import com.silqin.qanal.core.exception.ResourceNotFoundException;
import com.silqin.qanal.core.util.ApiResponse;

@RequestMapping("/collection")
@RestController
public class CollectionController {

    @GetMapping("run")
    public ResponseEntity<ApiResponse> run() {
        try {
            CoupangCollector coupangCollector = new CoupangCollector();
            coupangCollector.collect();

            ApiResponse<String> response = ApiResponse.success("Category is collected!");
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(), e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"));
            
        }
    }
    
    
}
