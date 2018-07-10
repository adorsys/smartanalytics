package de.adorsys.smartanalytics.pers.spi;

import de.adorsys.smartanalytics.api.config.ContractBlacklist;
import de.adorsys.smartanalytics.pers.api.ContractBlacklistEntity;

import java.util.Optional;

public interface ContractBlacklistRepositoryIf {

    Optional<ContractBlacklistEntity> getContractBlacklist();

    void saveContractBlacklist(ContractBlacklist contractBlackList);

}
