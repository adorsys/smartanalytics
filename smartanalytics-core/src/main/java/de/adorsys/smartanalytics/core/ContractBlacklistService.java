package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.api.config.ContractBlacklist;
import de.adorsys.smartanalytics.pers.api.ContractBlacklistEntity;
import de.adorsys.smartanalytics.pers.spi.ContractBlacklistRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContractBlacklistService {

    @Autowired
    private AnalyticsConfigProvider analyticsConfigProvider;
    @Autowired
    private ContractBlacklistRepositoryIf contractBlacklistRepository;
    @Autowired
    private StatusService statusService;

    public Optional<ContractBlacklistEntity> getContractBlacklist() {
        return contractBlacklistRepository.getContractBlacklist();
    }

    public void saveContractBlacklist(ContractBlacklist contractBlacklist) {
        contractBlacklistRepository.saveContractBlacklist(contractBlacklist);
        statusService.contractBlacklistChanged(contractBlacklist.getVersion());
        analyticsConfigProvider.initContractBlacklist();
    }
}
