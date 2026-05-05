package com.barak.game;

/**
 * Represents the elemental type of a monster and the game's win-loss logic.
 * Demonstrates the Single Responsibility Principle by encapsulating type advantages here.
 */
public enum ElementType {
    FIRE, WATER, GRASS, ELECTRIC;

    /**
     * Determines if this element defeats the target element.
     *
     * @param other The opposing element type.
     * @return true if this element has the advantage, false otherwise.
     */
    public boolean beats(ElementType other) {
        if (this == WATER && other == FIRE) return true;
        if (this == FIRE && other == GRASS) return true;
        if (this == GRASS && other == WATER) return true;
        if (this == ELECTRIC && other == WATER) return true;
        
        return false;
    }
}