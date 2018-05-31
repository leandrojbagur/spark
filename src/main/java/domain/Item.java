package domain;

import spark.utils.StringUtils;

import java.math.BigDecimal;
import java.util.List;

public class Item {
    private final String itemId;
    private final String title;
    private final String categoryId;
    private final BigDecimal price;
    private final String currencyId;
    private final int availableQuantity;
    private final String buyingMode;
    private final String listingTypeId;
    private final String condition;
    private final String description;
    private final String videoId;
    private final String warranty;
    private final List<String> pictures;

    public Item(ItemBuilder builder) {
        this.itemId = builder.itemId;
        this.title = builder.title;
        this.categoryId = builder.categoryId;
        this.price = builder.price;
        this.currencyId = builder.currencyId;
        this.availableQuantity = builder.availableQuantity;
        this.buyingMode = builder.buyingMode;
        this.listingTypeId = builder.listingTypeId;
        this.condition = builder.condition;
        this.description = builder.description;
        this.videoId = builder.videoId;
        this.warranty = builder.warranty;
        this.pictures = builder.pictures;
    }

    public String getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public String getBuyingMode() {
        return buyingMode;
    }

    public String getListingTypeId() {
        return listingTypeId;
    }

    public String getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getWarranty() {
        return warranty;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public boolean validate() {
         return (StringUtils.isNotEmpty(itemId) &&
                StringUtils.isNotEmpty(title) &&
                StringUtils.isNotEmpty(description) &&
                StringUtils.isNotEmpty(categoryId) &&
                (price != null && price.compareTo(BigDecimal.ZERO) >= 0));
    }

    public static class ItemBuilder {
        private String itemId;
        private String title;
        private String categoryId;
        private BigDecimal price;
        private String currencyId;
        private int availableQuantity;
        private String buyingMode;
        private String listingTypeId;
        private String condition;
        private String description;
        private String videoId;
        private String warranty;
        private List<String> pictures;

        public ItemBuilder itemId(String itemId) {
            this.itemId = itemId;
            return this;
        }

        public ItemBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ItemBuilder categoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public ItemBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ItemBuilder currencyId(String currencyId) {
            this.currencyId = currencyId;
            return this;
        }

        public ItemBuilder availableQuantity(int availableQuantity) {
            this.availableQuantity = availableQuantity;
            return this;
        }

        public ItemBuilder buyingMode(String buyingMode) {
            this.buyingMode = buyingMode;
            return this;
        }

        public ItemBuilder listingTypeId(String listingTypeId) {
            this.listingTypeId = listingTypeId;
            return this;
        }

        public ItemBuilder condition(String condition) {
            this.condition = condition;
            return this;
        }

        public ItemBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ItemBuilder videoId(String videoId) {
            this.videoId = videoId;
            return this;
        }

        public ItemBuilder warranty(String warranty) {
            this.warranty = warranty;
            return this;
        }

        public ItemBuilder pictures(List<String> pictures) {
            this.pictures = pictures;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }
}
