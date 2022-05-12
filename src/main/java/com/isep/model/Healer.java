package com.isep.model;

import java.util.ArrayList;

/**
 * Class Healer
 */
public class Healer extends SpellCaster {

    //
    // Fields
    //

    //
    // Constructors
    //
    public Healer (String name) {
        super(2, name, 50, 0, 5, new ArrayList<Potion>(){{add(new Potion()); add(new Potion());}}, new ArrayList<Food>());
    }

    //
    // Methods
    //

}
