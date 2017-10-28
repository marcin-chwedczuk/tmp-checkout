package pl.marcinchwedczuk.checkout3.impl;

import com.google.common.base.Strings;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(name="unit_price", nullable=false, precision=7, scale=2)
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
                "id=" + id +
                ", number='" + number + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }

    // ---------- getters / setters ------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
