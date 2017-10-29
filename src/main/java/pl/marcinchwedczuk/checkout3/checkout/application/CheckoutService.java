package pl.marcinchwedczuk.checkout3.checkout.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.marcinchwedczuk.checkout3.checkout.domain.*;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.CheckoutPricingData;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.DoubleSellDiscountApplier;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.QuantityDiscountApplier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class CheckoutService {
	public CheckoutService(
			CheckoutMapper checkoutMapper,
			QuantityDiscountRuleRepository quantityPricingRuleRepository,
			DoubleSellDiscountRuleRepository doubleSellDiscountRuleRepository,
			DoubleSellDiscountApplier doubleSellDiscountApplier,
			QuantityDiscountApplier quantityDiscountApplier)
	{
		this.checkoutMapper = checkoutMapper;
		this.quantityPricingRuleRepository = quantityPricingRuleRepository;
		this.doubleSellDiscountRuleRepository = doubleSellDiscountRuleRepository;
		this.doubleSellDiscountApplier = doubleSellDiscountApplier;
		this.quantityDiscountApplier = quantityDiscountApplier;
	}

	private final CheckoutMapper checkoutMapper;
	private final QuantityDiscountRuleRepository quantityPricingRuleRepository;
	private final DoubleSellDiscountRuleRepository doubleSellDiscountRuleRepository;
	private final DoubleSellDiscountApplier doubleSellDiscountApplier;
	private final QuantityDiscountApplier quantityDiscountApplier;

	@Transactional
	public CheckoutResponseDTO computePrices(CheckoutRequestDTO checkoutRequest) {
		CheckoutPricingData checkoutPricingData =
				checkoutMapper.mapToCheckoutPricingData(checkoutRequest);

		LocalDateTime checkoutTime = checkoutRequest.getRequestTime();

		applyQuantityDiscounts(checkoutTime, checkoutPricingData);
		applyDoubleSellDiscounts(checkoutTime, checkoutPricingData);

		CheckoutResponseDTO checkoutResponseDTO =
				checkoutMapper.mapToCheckoutResponseDTO(
						checkoutRequest.getRequestTime(), checkoutPricingData);

		return checkoutResponseDTO;
	}

	private void applyQuantityDiscounts(LocalDateTime requestTime, CheckoutPricingData checkoutPricingData) {
		Set<Long> itemIds = checkoutPricingData.getItemIds();

		List<QuantityDiscountRule> applicableRules =
				quantityPricingRuleRepository.findApplicableRules(requestTime, itemIds);

		quantityDiscountApplier.applyQuantityDiscounts(checkoutPricingData, applicableRules);
	}

	private void applyDoubleSellDiscounts(LocalDateTime requestTime, CheckoutPricingData checkoutPricingData) {
		Set<Long> itemIds = checkoutPricingData.getItemIds();

		List<DoubleSellDiscountRule> applicableRules =
				doubleSellDiscountRuleRepository.findApplicableRules(requestTime, itemIds);

		doubleSellDiscountApplier.applyDoubleSellDiscounts(checkoutPricingData, applicableRules);
	}
}
