package com.crafters.DataService.entities;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    @Id
    private String id;
    private String name;
    private String collectionName;
    private Map<String,Object> attributes;
    @DBRef
    private User user;
    private Map<Date,Integer> yearValue;
    @Nullable
    private String itemTotalId;
    private Date createdAt;
    private Date updatedAt;
}
