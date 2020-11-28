package com.gildedrose;

import lombok.AllArgsConstructor;
import lombok.With;

import static java.lang.Math.max;
import static java.lang.Math.min;

@With
@AllArgsConstructor
public class Item {
    public String name;
    public int sellIn, quality;

    public Item decreaseQuality(int quantity) {
        return withQuality(max(0, quality - quantity));
    }

    public Item increaseQuality(int quantity) {
        return withQuality(min(50, quality + quantity));
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
