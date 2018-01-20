package com.gitintheshell.geometrics.base;

import java.io.Serializable;

/**
 * Exception thrown by the Delaunay triangulator when it is initialized with
 * less than three points.
 * 
 * @author Semyon Danilov
 */
public class NotEnoughPointsException extends Exception implements Serializable {

    private static final long serialVersionUID = 7061712854155625067L;

    public NotEnoughPointsException() {
    }

    public NotEnoughPointsException(String s) {
        super(s);
    }

}