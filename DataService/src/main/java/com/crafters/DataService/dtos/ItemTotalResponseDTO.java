package com.crafters.DataService.dtos;

import com.crafters.DataService.entities.ItemTotal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ItemTotalResponseDTO {
    private String id;
    private String name;
    private Map<String, String> totalValue;

    public ItemTotalResponseDTO(ItemTotal itemTotal) {
        this.id = itemTotal.getId();
        this.name = itemTotal.getName();
        this.totalValue = itemTotal.getYearTotalValue();
    }
}
