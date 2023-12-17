package com.crafters.DataService.entities;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "items_total")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemTotal {
    @Id
    private String id;
    private String collectionName;
    private Object attribute;
    @DBRef
    private User user;
    @DBRef
    private List<Item> items;
    private Map<Date,Integer> yearTotalVale;
    private Date createdAt;
    private Date updatedAt;
}
