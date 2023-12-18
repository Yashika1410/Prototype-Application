package com.crafters.DataService.dtos;

import com.crafters.DataService.entities.Attribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ItemTotalRequestDTO {
    private String name;
    private Attribute attribute;
    private List<String> itemIds;
}
