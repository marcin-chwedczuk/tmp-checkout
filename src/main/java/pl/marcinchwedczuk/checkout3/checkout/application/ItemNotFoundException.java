package pl.marcinchwedczuk.checkout3.app;

public class ItemNotFoundException extends Exception {
	private String itemName;

	public ItemNotFoundException(String itemName) {
		super("Item '" + itemName + "' not found.");
		this.itemName = itemName;
	}

	public String getItemName() {
		return itemName;
	}
}
