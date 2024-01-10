package com.crafters.DataService.dtos;

import com.crafters.DataService.entities.Attribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ItemTotalUpdateRequestDTO {
    private String name;
    private Attribute attribute;
    private Map<String, Integer> yearTotalValue;
}
