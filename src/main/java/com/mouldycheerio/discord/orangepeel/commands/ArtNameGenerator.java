package com.mouldycheerio.discord.orangepeel.commands;

import java.util.ArrayList;
import java.util.Random;

import com.mouldycheerio.discord.orangepeel.commands.ArtCommand.ArtType;

public class ArtNameGenerator {
    public static String makename(ArtCommand.ArtType type) {
        Random random = new Random();
        if (type == ArtType.SCENE) {
            ArrayList<String> options1 = new ArrayList<String>();
            options1.add("the sunset on");
            options1.add("the sunrise on");
            options1.add("an orb in");
            options1.add("circle above");
            options1.add("a new dawn in");
            options1.add("the sun sets in");
            options1.add("the sun rises in");

            ArrayList<String> options2 = new ArrayList<String>();
            options2.add("the green place");
            options2.add("a field");
            options2.add("greenland");
            options2.add("sunriseland");
            options2.add("dipshit");
            options2.add("green");
            options2.add("Suqarey");
            options2.add("the greeness");

            options1.add("the world");
            options1.add("planet earth");
            return options1.get(random.nextInt(options1.size())) + " " + options2.get(random.nextInt(options2.size()));

        }

        if (type == ArtType.SQUARES || type == ArtType.CIRCLES || type == ArtType.PHOTO) {
            ArrayList<String> options1 = new ArrayList<String>();
            options1.add("an excited");
            options1.add("random");
            options1.add("Big");
            options1.add("Large");
            options1.add("Grand");
            options1.add("Enormous");
            options1.add("Huge");
            options1.add("Ogarly");
            options1.add("Medium");
            options1.add("Lame");
            options1.add("Small");
            options1.add("Tiny");
            options1.add("Petit");
            options1.add("Miniscule");
            options1.add("Mini");
            options1.add("Very");
            options1.add("a happy");
            options1.add("a sad");
            options1.add("The");
            options1.add("Strange");
            options1.add("The abstract");
            options1.add("floating");
            options1.add("sneezing");
            options1.add("Cat");
            options1.add("Dog");
            options1.add("Colourfull");
            options1.add("unknown");
            options1.add("memely");
            options1.add("Yellow");
            options1.add("Green");
            options1.add("Cyan");
            options1.add("Blue");
            options1.add("Purple");
            options1.add("Red");
            options1.add("Magic");
            options1.add("Mythical");
            options1.add("Cool");
            options1.add("The cool");
            options1.add("The worst");
            options1.add("The best");
            options1.add("The adpoted");
            options1.add("Irrelavent");
            options1.add("Random");
            options1.add("bork");
            options1.add("Snoop");
            options1.add("Mr");
            options1.add("Mrs");
            options1.add("Sir");
            options1.add("Lady");
            options1.add("Lord");
            options1.add("king");
            options1.add("a piece of");
            options1.add("a small");
            options1.add("a very large");
            options1.add("a Strange object that looks like a");

            ArrayList<String> options2 = new ArrayList<String>();
            options2.add("Dog");
            options2.add("Cat");
            options2.add("Banana");
            options2.add("World");
            options2.add("Creature");
            options2.add("Monster");
            options2.add("Figit Spinner");
            options2.add("Orange");
            options2.add("Robot");
            options2.add("Art work");
            options2.add("art");
            options2.add("creativity");
            options2.add("rose");
            options2.add("Flower");
            options2.add("Mr Mofrad");
            options2.add("png file");
            options2.add("Teacher");
            options2.add("Dice");
            options2.add("Plane");
            options2.add("Car");
            options2.add("Bike");
            options2.add("bork");
            options2.add("Dogg");
            options2.add("Authur");
            options2.add("David");
            options2.add("Kai");
            options2.add("Kai");
            options2.add("Kelwing");
            options2.add("Orange");
            options2.add("Peel");
            options2.add("Robbie");
            options2.add("Art");
            options2.add("Painting");
            options2.add("pixels");
            options2.add("image");
            options2.add("abstract");
            options2.add("art");
            options2.add("magic");
            options2.add("Magic");
            options2.add("Mythicality");
            options2.add("Flower pot");
            options2.add(">art");
            options2.add("memes");
            options2.add("apples");
            options2.add("pears");
            options2.add("computers");
            options2.add("A");
            options2.add("B");
            options2.add("C");
            options2.add("number");
            options2.add("Writings");
            options2.add("Crap");
            return options1.get(random.nextInt(options1.size())) + " " + options2.get(random.nextInt(options2.size()));

        }
        return "Artwork";
    }
}
