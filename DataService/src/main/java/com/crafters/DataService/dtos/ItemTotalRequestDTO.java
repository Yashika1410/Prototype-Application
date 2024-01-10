package com.crafters.DataService.dtos;

import com.crafters.DataService.entities.Attribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ItemTotalRequestDTO {
    private String name;
    private String rowType;
    private Attribute attribute;
    private List<String> itemIds;
    private Map<String, Double> yearTotalValue;
}
