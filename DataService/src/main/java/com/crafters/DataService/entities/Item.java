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

@Document(collection = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    /**
     *
     */
    @Id
    private String id;
    /**
     *
     */
    private String name;
    private String rowType;
    /**
     *
     */
    private String collectionName;
    /**
     *
     */
    private Map<String, Object> attributes;
    /**
     *
     */
    @DBRef
    private User user;
    /**
     *
     */
    private Map<String, Integer> yearValue;
    /**
     *
     */
    @DBRef
    private List<ItemTotal> itemTotals;
    /**
     *
     */
    private Date createdAt;
    /**
     *
     */
    private Date updatedAt;

}
