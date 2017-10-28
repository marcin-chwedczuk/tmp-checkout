package pl.marcinchwedczuk.checkout3;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class DevelopmentService {
	public DevelopmentService(ItemRepository itemRepository) {
		this.itemRepository = Objects.requireNonNull(itemRepository);
	}

	private final ItemRepository itemRepository;

	@Transactional
	public void setupTestData() {
		Item itemA = new Item("item_a", new BigDecimal("100.00"));
		itemRepository.save(itemA);

		Item itemB = new Item("item_b", new BigDecimal("200.00"));
		itemRepository.save(itemB);
	}
}
