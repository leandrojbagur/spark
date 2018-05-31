package service;

import domain.Item;
import exception.ItemException;

import java.util.List;

public interface IItemService {
    void save(String json) throws ItemException;
    void update(String itemId, String json) throws ItemException;
    Item get(String id) throws ItemException;
    List<Item> getAll() throws ItemException;
    void delete(String item) throws ItemException;
}
