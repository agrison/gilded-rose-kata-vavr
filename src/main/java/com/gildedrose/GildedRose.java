package com.gildedrose;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;

import java.util.function.Function;

import static io.vavr.API.*;
import static io.vavr.Function1.identity;

class GildedRose {
    static final String BACKSTAGE = "Backstage passes to a TAFKAL80ETC concert";
    static final String AGED_BRIE = "Aged Brie";
    static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    Seq<Item> items;

    public GildedRose(Item[] items) {
        this.items = Vector.of(items);
    }

    Function<Item, Item> agedBrie = item ->
            item.increaseQuality(item.sellIn < 0 ? 2 : 1) : item;

    Function<Item, Item> defaultItem = item ->
            item.decreaseQuality(item.sellIn <= 0 ? 2 : 1) : item;

    Function<Item, Item> backStage = item ->
            Match(item).of(Case($(i -> i.sellIn < 0), item::resetQuality),
                    Case($(i -> i.sellIn < 5), () -> item.increaseQuality(3)),
                    Case($(i -> i.sellIn < 10), () -> item.increaseQuality(2)),
                    Case($(), () -> item.increaseQuality(1)));

    Map<String, Function<Item, Item>> updater =
            HashMap.of(SULFURAS, identity(), BACKSTAGE, backStage, AGED_BRIE, agedBrie);

    Function<Item, Item> ageItem = item ->
            item.name.equals(SULFURAS) ? item : item.decreaseSellIn();

    public void updateQuality() {
        items = items.map(it -> ageItem.andThen(updater.getOrElse(it.name, defaultItem)).apply(it));
    }
}

