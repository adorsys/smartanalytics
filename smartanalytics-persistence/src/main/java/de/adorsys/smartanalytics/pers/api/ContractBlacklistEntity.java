package de.adorsys.smartanalytics.pers.api;

import de.adorsys.smartanalytics.api.config.ContractBlacklist;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = false)
@Document
public class ContractBlacklistEntity extends ContractBlacklist {

    public static final String CONTAINER_ID = "contract-blacklist";

    @Id
    private String id;
}
