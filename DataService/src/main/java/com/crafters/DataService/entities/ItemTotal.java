package com.crafters.DataService.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(collection = "items_total")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemTotal {
    @Id
    private String id;
    private String name;
    private Attribute attribute;
    @DBRef
    private User user;
    @DBRef
    private List<Item> items;
    private Map<String, String> yearTotalValue;
    private Date createdAt;
    private Date updatedAt;
}
