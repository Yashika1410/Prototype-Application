package com.crafters.DataService.dtos;

import com.crafters.DataService.entities.Attribute;
import com.crafters.DataService.entities.Item;
import com.crafters.DataService.entities.ItemTotal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ItemTotalResponseDTO {
    private String id;
    private String name;
    private Attribute attribute;
    private List<ItemResponse> listOfItems;
    private Map<String, Integer> totalValue;

    public ItemTotalResponseDTO(ItemTotal itemTotal) {
        this.id = itemTotal.getId();
        this.name = itemTotal.getName();
        this.totalValue = itemTotal.getYearTotalValue();
        this.attribute = itemTotal.getAttribute();
        this.listOfItems = itemTotal.getItems().stream(
        ).map(ItemResponse::new).toList();
    }
}
