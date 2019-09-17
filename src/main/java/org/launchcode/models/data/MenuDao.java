package org.launchcode.models.data;

import org.launchcode.models.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.launchcode.models.Menu;
import javax.transaction.Transactional;

@Repository
@Transactional
public interface MenuDao extends CrudRepository<Menu, Integer> {

}
