package com.crafters.DataService.dtos;

import com.crafters.DataService.entities.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponse {
    private String id;
    private String name;
    private String rowType;
    private String collectionName;
    private Map<String, Object> attributes;
    private Map<String, Integer> yearValue;

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.collectionName = item.getCollectionName();
        this.attributes = item.getAttributes();
        this.yearValue = item.getYearValue();
        this.rowType = item.getRowType();
    }
}
