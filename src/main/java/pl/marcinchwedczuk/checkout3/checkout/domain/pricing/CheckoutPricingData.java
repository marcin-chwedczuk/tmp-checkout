package pl.marcinchwedczuk.checkout3.checkout.domain.pricing;

import pl.marcinchwedczuk.checkout3.checkout.domain.Item;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableSet;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class CheckoutPricingData {
	private final Map<Long, ItemPricingData> linesByItemId;

	public CheckoutPricingData(Collection<? extends ItemPricingData> lines) {
		linesByItemId = lines.stream()
				.collect(toMap(ItemPricingData::getItemId, identity()));
	}

	public ItemPricingData findPricingDataForItem(Item item) {
		return linesByItemId.get(item.getId());
	}

	public Set<Long> getItemIds() {
		return unmodifiableSet(linesByItemId.keySet());
	}

	public Collection<ItemPricingData> lines() {
		return unmodifiableCollection(linesByItemId.values());
	}
}
