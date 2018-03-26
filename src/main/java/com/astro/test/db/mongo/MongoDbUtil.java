package com.astro.test.db.mongo;

import com.astro.test.db.IDbUtil;
import com.astro.test.util.ConfigurationReader;
import com.astro.test.util.Constants;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import java.io.IOException;

/**
 * Created by sahanlm on 3/23/18.
 */
public class MongoDbUtil implements IDbUtil<MongoDatabase> {

    private static final Logger logger = LogManager.getLogger(MongoDbUtil.class);

    private static MongoDbUtil _instance;
    private ConfigurationReader configurationReader;
    private MongoClient _client;
    private MongoDbUtil() throws IOException {
        this.configure();
    }

    public static MongoDbUtil getInstance() throws IOException {
        if (_instance == null) {
            _instance = new MongoDbUtil();
        }

        return _instance;
    }

    private void configure() throws IOException {
        this.configurationReader = ConfigurationReader.getInstance();
        String connectionUrl = this.configurationReader.getConfig(Constants.CONN_URL);
        this._client = new MongoClient(new MongoClientURI(connectionUrl));
    }


    public MongoDatabase getDbInstance() {
        return this.getDbInstance(this.configurationReader.getConfig(Constants.DEFAULT_DB));
    }

    public MongoDatabase getDbInstance(String databaseName) {
        if (_client == null) {
            return null;
        }
        return _client.getDatabase(databaseName);
    }

    @Override
    protected void finalize() throws Throwable {
        this._client.close();
    }

    public static Document convertToBasicDbObject(Object object) {
        Gson gson = new Gson();
        Document document = Document.parse(gson.toJson(object));
        return document;
    }
}
