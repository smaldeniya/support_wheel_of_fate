package com.astro.test.db;

import java.util.List;

/**
 * Created by sahanlm on 3/23/18.
 */
public interface IRepository <T> {

    String add(T item);

    void addAll(List<T> items);

    void update(T item);

    void remove(T item);

    List<T> query(ISpecification ISpecification);
}
