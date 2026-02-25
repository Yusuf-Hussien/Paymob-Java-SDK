package com.paymob.sdk.models.common;

/**
 * Item details for payment intention requests.
 * Amount sum validation: total amount must equal sum of all item amounts.
 */
public class Item {
    private String name;
    private int amount; // in cents
    private String description;
    private int quantity;

    public Item() {
    }

    public Item(String name, int amount, String description, int quantity) {
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.quantity = quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the total amount for this item (amount * quantity).
     * 
     * @return Total amount in cents
     */
    public int getTotalAmount() {
        return amount * quantity;
    }

    public static class Builder {
        private String name;
        private int amount;
        private String description;
        private int quantity;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Item build() {
            return new Item(name, amount, description, quantity);
        }
    }
}
