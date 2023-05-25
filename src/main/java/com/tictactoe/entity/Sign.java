package com.tictactoe.entity;

public enum Sign {
    EMPTY(' '),
    CROSS('X'),
    NOUGHT('0');

    private final char sign;

    Sign(char sign) {
        this.sign = sign;
    }


    // used in index.jsp
    @SuppressWarnings("unused")
    public char getSign() {
        return sign;
    }
}