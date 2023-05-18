package com.project.gacha;

public class Inventory {
    final int record;
    final int id;
    final int image;
    final int rarity;
    final String name;

    public Inventory(int recordCard ,int idCard, int imageCard, int rarityCard, String name) {
        this.record = recordCard;
        this.id = idCard;
        image = imageCard;
        this.rarity = rarityCard;
        this.name = name;
    }

    public int getRecord() {
        return record;
    }

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public int getRarity() {
        return rarity;
    }

    public String getName() {
        return name;
    }
}
