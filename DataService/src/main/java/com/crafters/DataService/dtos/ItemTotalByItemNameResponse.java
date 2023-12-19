package com.crafters.DataService.dtos;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemTotalByItemNameResponse {
     private String name;
    private String collectionName;
    private Map<String, Integer> yearValue;
}
