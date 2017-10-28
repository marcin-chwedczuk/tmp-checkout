package pl.marcinchwedczuk.checkout3;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
	List<Item> findByNumber(String number);
}

