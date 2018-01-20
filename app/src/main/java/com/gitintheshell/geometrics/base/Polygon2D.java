package com.gitintheshell.geometrics.base;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created by Semyon on 20.01.2018.
 */

public class Polygon2D implements Iterable<Vector2D> {

    List<Vector2D> vertices;

    public Polygon2D(final List<Vector2D> vertices) {
        this.vertices = vertices;
    }

    public List<Vector2D> getVertices() {
        return vertices;
    }

    @Override
    public Iterator<Vector2D> iterator() {
        return vertices.iterator();
    }

    @Override
    public void forEach(final Consumer<? super Vector2D> action) {
        vertices.forEach(action);
    }

    @Override
    public Spliterator<Vector2D> spliterator() {
        return vertices.spliterator();
    }
}
