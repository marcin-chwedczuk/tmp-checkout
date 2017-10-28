package pl.marcinchwedczuk.checkout3.checkout.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ItemRepository extends CrudRepository<Item, Long> {
	Optional<Item> findByNumber(String number);
	List<Item> findAllByNumbers(Collection<String> numbers);
}

