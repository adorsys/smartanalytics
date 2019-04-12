package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.api.config.ContractBlacklist;
import de.adorsys.smartanalytics.pers.spi.ContractBlacklistRepositoryIf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContractBlacklistService {

    private final AnalyticsConfigProvider analyticsConfigProvider;
    private final ContractBlacklistRepositoryIf contractBlacklistRepository;
    private final StatusService statusService;

    public void saveContractBlacklist(ContractBlacklist contractBlacklist) {
        contractBlacklistRepository.saveContractBlacklist(contractBlacklist);
        statusService.contractBlacklistChanged(contractBlacklist.getVersion());
        analyticsConfigProvider.initContractBlacklist();
    }
}
