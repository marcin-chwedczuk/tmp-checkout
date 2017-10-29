package pl.marcinchwedczuk.checkout3.checkout.domain.pricing;

import com.google.common.base.Preconditions;
import pl.marcinchwedczuk.checkout3.checkout.domain.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static pl.marcinchwedczuk.checkout3.checkout.utils.BigDecimals.summingBigDecimal;

public class ItemPricingData {
	public static ItemPricingData fromItemAndTotalQuantity(Item item, BigDecimal totalQuantity) {
		return new ItemPricingData(item, totalQuantity);
	}

	private ItemPricingData(Item item, BigDecimal totalQuantity) {
		this.item = item;
		this.totalQuantity = totalQuantity;
	}

	private final Item item;
	private final BigDecimal totalQuantity;

	private final List<ItemPricingSegment> pricingSegments = new ArrayList<>();
	private BigDecimal unitPriceAfterQuantityDiscount;

	public BigDecimal getOriginalUnitPrice() {
		return item.getUnitPrice();
	}
	public Long getItemId() { return item.getId(); }

	public boolean hasQuantityDiscount() {
		return getUnitPriceAfterQuantityDiscount() != null;
	}

	public BigDecimal computeQuantityWithoutDoubleSellDiscount() {
		BigDecimal doubleSellDiscountedQuantity = pricingSegments.stream()
				.map(ItemPricingSegment::getQuantity)
				.collect(summingBigDecimal());

		return totalQuantity.subtract(doubleSellDiscountedQuantity);
	}

	public void createDiscountSegment(BigDecimal discountableQty, BigDecimal discountedPrice) {
		BigDecimal totalDiscounted = discountableQty.add(computeQuantityWithoutDoubleSellDiscount());

		Preconditions.checkArgument(
			totalDiscounted.compareTo(totalQuantity) <= 0,
			"Total discounted quantity must be lower than totalQuantity.");

		Preconditions.checkArgument(
				discountedPrice.compareTo(unitPriceAfterQuantityDiscount) < 0,
				"Segment may be created only when discount yields lower price.");

		// Merge segments with the same price

		ItemPricingSegment segmentWithSamePrice = pricingSegments.stream()
				.filter(segment -> segment.getDiscountedPrice().equals(discountedPrice))
				.findFirst().orElse(null);

		if (segmentWithSamePrice != null) {
			pricingSegments.remove(segmentWithSamePrice);

			pricingSegments.add(
				new ItemPricingSegment(
						segmentWithSamePrice.getQuantity().add(discountableQty),
						segmentWithSamePrice.getDiscountedPrice()));
		}
		else {
			pricingSegments.add(
				new ItemPricingSegment(discountableQty, discountedPrice));
		}
	}

	// getter / setter ------------------------------------------

	public Item getItem() {
		return item;
	}

	public BigDecimal getTotalQuantity() {
		return totalQuantity;
	}

	public void setUnitPriceAfterQuantityDiscount(BigDecimal unitPriceAfterQuantityDiscount) {
		this.unitPriceAfterQuantityDiscount = unitPriceAfterQuantityDiscount;
	}

	public BigDecimal getUnitPriceAfterQuantityDiscount() {
		return unitPriceAfterQuantityDiscount;
	}
}
