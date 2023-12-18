package com.crafters.DataService.dtos;

import java.util.Date;
import java.util.Map;

import com.crafters.DataService.entities.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponse {
    private String id;
    private String name;
    private String collectionName;
    private Map<String,Object> attributes;
    private Map<String,String> yearValue;

    public ItemResponse(Item item){
        this.id=item.getId();
        this.name=item.getName();
        this.collectionName=item.getCollectionName();
        this.attributes=item.getAttributes();
        this.yearValue=item.getYearValue();
    }
}
