package com.example;

public class SuggestedExchange {
    private final Property property1;
    private final Property property2;
    private final double gain;
    private final double probabilityOfTrade;

    public SuggestedExchange(Property property1, Property property2, double gain, double probabilityOfTrade) {
        this.property1 = property1;
        this.property2 = property2;
        this.gain = gain;
        this.probabilityOfTrade = probabilityOfTrade;
    }

    public double getGain() {
        return gain;
    }

    public double getProbabilityOfTrade() {
        return probabilityOfTrade;
    }

    public Property getProperty1() {
        return property1;
    }

    public Property getProperty2() {
        return property2;
    }

    @Override
    public String toString() {
        return String.format("Exchange %s ↔ %s [Gain: %.2f, Probability of Trade: %.2f]",
                property1, property2, gain, probabilityOfTrade);
    }
}