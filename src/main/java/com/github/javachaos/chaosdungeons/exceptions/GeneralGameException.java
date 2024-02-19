package com.github.javachaos.chaosdungeons.exceptions;

public class GeneralGameException extends RuntimeException {
    public GeneralGameException(Exception e) {
        super(e);
    }

    public GeneralGameException(final String msg) {
        super(msg);
    }
}
