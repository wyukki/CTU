package cz.cvut.kbss.ear.eshop.service;

import cz.cvut.kbss.ear.eshop.dao.ItemDao;
import cz.cvut.kbss.ear.eshop.model.Item;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemDao dao;

    public ItemService(ItemDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public Item find(Integer id) {
        return dao.find(id);
    }
}
