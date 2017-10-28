package pl.marcinchwedczuk.checkout3.checkout.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class PricingRule extends BaseEntity {
	@Column(name = "valid_from", nullable = true)
	private LocalDateTime validFrom;

	@Column(name = "valid_to", nullable = true)
	private LocalDateTime validTo;

	public boolean isApplicableAtDate(LocalDateTime date) {
		if ((validFrom != null) && date.isBefore(validFrom))
			return false;

		if ((validTo != null) && date.isAfter(validTo))
			return false;

		return true;
	}

	// getter / setters

	public LocalDateTime getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDateTime validFrom) {
		this.validFrom = validFrom;
	}

	public LocalDateTime getValidTo() {
		return validTo;
	}

	public void setValidTo(LocalDateTime validTo) {
		this.validTo = validTo;
	}
}
