package com.crafters.DataService.dtos;

import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateItemRequestDTO {
    private String name;
    private String collectionName;
    private Map<String,Object> attributes;
    private Map<String, Integer> yearValue;
}
