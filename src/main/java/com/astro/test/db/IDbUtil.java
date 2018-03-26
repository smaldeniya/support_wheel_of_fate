package com.astro.test.db;

/**
 * Created by sahanlm on 3/23/18.
 */
public interface IDbUtil<T> {

    T getDbInstance();

    T getDbInstance(String databaseName);

}
