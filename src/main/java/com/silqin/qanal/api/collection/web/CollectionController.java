package com.silqin.qanal.api.collection.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;



@RequestMapping("/collection")
@RestController
public class CollectionController {

    @GetMapping("run")
    public String run(@RequestParam String categoryId) {
        if(categoryId == null) {
            return new String();
        }

        
        
        return new String();
    }
    
    
}
