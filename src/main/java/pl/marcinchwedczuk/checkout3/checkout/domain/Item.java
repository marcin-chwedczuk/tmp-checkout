package pl.marcinchwedczuk.checkout3.checkout.domain;

import com.google.common.base.Strings;

import javax.persistence.*;
import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

@Entity
@Table(name = "item",
	uniqueConstraints = {
		@UniqueConstraint(name = "uc_item_number", columnNames = { "item_number" })
	})
public class Item extends BaseEntity {

	// Use different column name to make adhoc queries easier
	// because 'number' is reserved SQL keyword.
    @Column(name = "item_number", nullable = false)
    private String number;

    @Column(name="unit_price", nullable=false,
			precision= MONETARY_PRECISION, scale= MONETARY_SCALE)
    private BigDecimal unitPrice;

    protected Item() {}

    public Item(String number, BigDecimal unitPrice) {
        if (Strings.isNullOrEmpty(number))
            throw new IllegalArgumentException("Empty item number.");

        if (unitPrice.compareTo(ZERO) < 0)
            throw new IllegalArgumentException("Unit price cannot be less than zero.");

        this.number = number;
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + getId() +
                ", number='" + number + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }

    // ---------- getters / setters ------------------------------

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
