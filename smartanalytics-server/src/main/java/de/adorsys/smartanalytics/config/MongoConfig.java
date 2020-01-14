package de.adorsys.smartanalytics.config;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.util.Optional;

@RequiredArgsConstructor
@Configuration
@EnableSmartanalyticsMongoPersistence
@PropertySource(value = "${mongo.properties.url}", ignoreResourceNotFound = true)
@Profile({"mongo-persistence"})
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final Environment env;

    @Override
    protected String getDatabaseName() {
        return Optional.ofNullable(env.getProperty("mongo.databaseName"))
            .orElseThrow(() -> new IllegalStateException("missing env property mongo.databaseName"));
    }

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    private MongoCredential createMongoCredential() {
        String userName = Optional.ofNullable(env.getProperty("mongo.userName"))
            .orElseThrow(() -> new IllegalStateException("missing env property mongo.userName"));

        String databaseName = Optional.ofNullable(env.getProperty("mongo.databaseName"))
            .orElseThrow(() -> new IllegalStateException("missing env property mongo.databaseName"));

        String password = Optional.ofNullable(env.getProperty("mongo.password"))
            .orElseThrow(() -> new IllegalStateException("missing env property mongo.password"));

        return MongoCredential.createCredential(userName, databaseName,
            password.toCharArray());
    }

    private ConnectionString getConnectionString() {
        return Optional.ofNullable(env.getProperty("mongo.server"))
            .map(server -> server.startsWith("mongodb://") ? server : "mongodb://" + server)
            .map(ConnectionString::new)
            .orElseThrow(() -> new IllegalStateException("missing env property mongo.server"));
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        MongoClientSettings.Builder mongoClientSettingsBuilder = MongoClientSettings.builder()
            .applyConnectionString(getConnectionString())
            .writeConcern(WriteConcern.JOURNALED)
            .readPreference(ReadPreference.secondaryPreferred());

        if (StringUtils.isEmpty(env.getProperty("mongo.userName"))) {
            return MongoClients.create(mongoClientSettingsBuilder.build());
        } else {
            return MongoClients.create(mongoClientSettingsBuilder.credential(createMongoCredential()).build());
        }
    }

    @Override
    public MongoDbFactory mongoDbFactory() {
        return Optional.ofNullable(env.getProperty("mongo.databaseName"))
            .map(databaseName -> new SimpleMongoClientDbFactory(mongoClient(), databaseName))
            .orElseThrow(() -> new IllegalStateException("missing env property mongo.databaseName"));

    }
}


