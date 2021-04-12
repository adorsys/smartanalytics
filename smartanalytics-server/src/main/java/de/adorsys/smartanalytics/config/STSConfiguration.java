package de.adorsys.smartanalytics.config;

import de.adorsys.sts.decryption.EnableDecryption;
import de.adorsys.sts.keyrotation.EnableKeyRotation;
import de.adorsys.sts.persistence.mongo.config.EnableMongoPersistence;
import de.adorsys.sts.pop.EnablePOP;
import de.adorsys.sts.token.authentication.EnableTokenAuthentication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!disable-security")
@Configuration
@EnableTokenAuthentication
@EnablePOP
@EnableDecryption
@EnableKeyRotation
@EnableMongoPersistence
public class STSConfiguration {
}
