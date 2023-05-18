package com.project.gacha;

public class Card {
    final int id;
    final int image;
    final int rarity;
    final String name;


    public Card(int imageCard, int idCard, int rarityCard, String name) {
        image = imageCard;
        this.id = idCard;
        this.rarity = rarityCard;
        this.name = name;
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
