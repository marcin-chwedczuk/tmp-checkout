package pl.marcinchwedczuk.checkout3.checkout.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
	protected static final int MONETARY_PRECISION = 7;
	protected static final int MONETARY_SCALE = 2;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	// getter / setter -----------------------------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
