package com.gildedrose;

import lombok.AllArgsConstructor;
import lombok.With;

import static java.lang.Math.max;
import static java.lang.Math.min;

@With
@AllArgsConstructor
public class Item {
    static final int MIN_QUALITY = 0;
    static final int MAX_QUALITY = 50;
    final String name;
    int sellIn, quality;

    public Item decreaseQuality(int quantity) {
        return withQuality(max(MIN_QUALITY, quality - quantity));
    }

    public Item increaseQuality(int quantity) {
        return withQuality(min(MAX_QUALITY, quality + quantity));
    }

    public Item resetQuality() {
        return withQuality(0);
    }

    public Item decreaseSellIn() {
        return withSellIn(sellIn - 1);
    }

    @Override
    public String toString() {
        return this.name + ", " + this.sellIn + ", " + this.quality;
    }
}
