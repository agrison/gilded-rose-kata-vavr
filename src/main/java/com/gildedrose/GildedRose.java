package com.gildedrose;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;

import java.util.function.Function;

import static io.vavr.API.*;
import static io.vavr.Function1.identity;

/**
 * This is a solution implementation of the Gilded Rose Kata in a functional way
 * using Java and the Vavr library (see https://github.com/vavr-io/vavr).
 *
 * The implementation is based on:
 *   - a specific and separate method for each of the item types
 *   - a function call dispatch using a HashMap (see the updater function)
 *   - the Vavr's Match API to replace an if/else or switch statement
 *   - function composition (see the updateItem function)
 *   - an immutable Seq instead of an array of Item
 *   - replacing a big for loop with a sequence iteration
 *   - Lombok to make Item immutable but avoid boilerplate
 *
 * This kata solution is available at: https://github.com/agrison/gilded-rose-kata-vavr
 * Website: https://grison.me
 * Github: https://github.com/agrison
 * @author Alexandre Grison
 * @implNote Thanks to Yoan Thirion for the approval tests (https://github.com/ythirion/GildedRose-kata)
 */
class GildedRose {
    final String AGED_BRIE = "Aged Brie";
    final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    final String BACKSTAGE = "Backstage passes to a TAFKAL80ETC concert";
    Seq<Item> items;

    public GildedRose(Item[] items) {
        this.items = Vector.of(items);
    }

    // All items decrease in sell in, except Sulfuras because it's a legend
    Function<Item, Item> ageItem = item ->
            item.name.equals(SULFURAS) ? item : item.decreaseSellIn();

    // Aged Brie actually increases in Quality the older it gets
    Function<Item, Item> agedBrie = item ->
            item.increaseQuality(item.sellIn < 0 ? 2 : 1);

    // Any other item degrade in Quality twice as fast as normal items
    Function<Item, Item> defaultItem = item ->
            item.decreaseQuality(item.sellIn <= 0 ? 2 : 1);

    // Backstage passes, increases in Quality as it's SellIn value approaches;
    // Quality increases by 2 when there are 10 days or less and by 3 when there are 5 days or less
    // but Quality drops to 0 after the concert
    Function<Item, Item> backStage = item ->
            Match(item).of(Case($(i -> i.sellIn < 0), item::resetQuality),
                    Case($(i -> i.sellIn < 5), () -> item.increaseQuality(3)),
                    Case($(i -> i.sellIn < 10), () -> item.increaseQuality(2)),
                    Case($(), () -> item.increaseQuality(1)));

    // dispatch function call depending on an item name
    Map<String, Function<Item, Item>> updater =
            HashMap.of(SULFURAS, identity(), BACKSTAGE, backStage, AGED_BRIE, agedBrie);

    Function<Item, Item> updateItem = item ->
            ageItem.andThen(updater.getOrElse(item.name, defaultItem)).apply(item);

    public void updateQuality() {
        items = items.map(updateItem);
    }
}
