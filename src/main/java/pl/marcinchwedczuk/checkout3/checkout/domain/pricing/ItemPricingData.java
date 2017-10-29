package pl.marcinchwedczuk.checkout3.checkout.domain.pricing;

import com.google.common.base.Preconditions;
import pl.marcinchwedczuk.checkout3.checkout.domain.Item;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Comparator.comparing;
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

	public BigDecimal computeQuantityCoveredByDoubleSellDiscount() {
		BigDecimal quantity = pricingSegments.stream()
				.map(ItemPricingSegment::getQuantity)
				.collect(summingBigDecimal());

		return quantity;
	}

	public BigDecimal computeQuantityNotCoveredByDoubleSellDiscount() {
		BigDecimal coveredQuantity = computeQuantityCoveredByDoubleSellDiscount();
		return totalQuantity.subtract(coveredQuantity);
	}

	public void createDiscountSegmentFromQtyAndPrice(BigDecimal segmentQuantity, BigDecimal segmentPrice) {
		BigDecimal totalDiscounted = segmentQuantity.add(
				computeQuantityCoveredByDoubleSellDiscount());

		Preconditions.checkArgument(
			totalDiscounted.compareTo(totalQuantity) <= 0,
			"Total discounted quantity must be lower than totalQuantity.");

		Preconditions.checkArgument(
				segmentPrice.compareTo(unitPriceAfterQuantityDiscount) < 0,
				"Segment may be created only when discount yields lower price.");

		// Merge segments with the same price

		ItemPricingSegment segmentWithSamePrice = pricingSegments.stream()
				.filter(segment -> segment.getDiscountedPrice().equals(segmentPrice))
				.findFirst().orElse(null);

		if (segmentWithSamePrice != null) {
			pricingSegments.remove(segmentWithSamePrice);

			pricingSegments.add(
				new ItemPricingSegment(
						segmentWithSamePrice.getQuantity().add(segmentQuantity),
						segmentWithSamePrice.getDiscountedPrice()));
		}
		else {
			pricingSegments.add(
				new ItemPricingSegment(segmentQuantity, segmentPrice));
		}
	}

	public List<ItemPricingSegment> asSegments() {
		List<ItemPricingSegment> segments = new ArrayList<>();

		segments.addAll(pricingSegments);

		// If there is still some qty not covered by double sell discounts
		// create a separate segment for it.
		BigDecimal qtyNotCoveredBySegments = computeQuantityNotCoveredByDoubleSellDiscount();
		if (qtyNotCoveredBySegments.compareTo(BigDecimal.ZERO) > 0) {
			segments.add(new ItemPricingSegment(
					qtyNotCoveredBySegments, getUnitPriceAfterQuantityDiscount()));
		}

		segments.sort(
				comparing(ItemPricingSegment::getQuantity)
					.thenComparing(ItemPricingSegment::getDiscountedPrice).reversed());

		return segments;
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
