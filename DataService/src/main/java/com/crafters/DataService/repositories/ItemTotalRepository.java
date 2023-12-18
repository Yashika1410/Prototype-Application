package com.crafters.DataService.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.crafters.DataService.entities.Item;
import com.crafters.DataService.entities.ItemTotal;
/**
 * Repository interface for performing database operations related to the ItemTotal entity.
 */
public interface ItemTotalRepository extends MongoRepository<ItemTotal, String> {
    /**
     * Retrieves a list of items based on the specified user ID.
     *
     * @param userId The user ID for which to retrieve items.
     * @return A list of items associated with the specified user ID.
     */
    List<Item> findByUser(String userId);
}
