package pl.marcinchwedczuk.checkout3.checkout.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
	Item findByNumber(String number);

	// TODO: FetchEager may cause Items to be duplicated in returned list.
	List<Item> findAllByNumberIn(Collection<String> numbers);
}

