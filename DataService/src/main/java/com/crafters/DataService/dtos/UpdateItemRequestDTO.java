package com.crafters.DataService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemRequestDTO {
    private String id;
    private String name;
    private String rowType;
    private String collectionName;
    private Map<String,Object> attributes;
    private Map<String, Integer> yearValue;
}
