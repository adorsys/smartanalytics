package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.api.config.ContractBlacklist;
import de.adorsys.smartanalytics.pers.api.ContractBlacklistEntity;
import de.adorsys.smartanalytics.pers.spi.ContractBlacklistRepositoryIf;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Profile({"mongo-persistence", "fongo"})
@Service
public class ContractBlacklistRepositoryImpl implements ContractBlacklistRepositoryIf {

    @Autowired
    private ContractBlacklistRepositoryMongodb contractBlacklistRepository;

    @Override
    public Optional<ContractBlacklistEntity> getContractBlacklist() {
        return contractBlacklistRepository.findById(ContractBlacklistEntity.CONTAINER_ID);
    }

    @Override
    public void saveContractBlacklist(ContractBlacklist groupsContainer) {
        ContractBlacklistEntity containerEntity = new ContractBlacklistEntity();
        BeanUtils.copyProperties(groupsContainer, containerEntity);

        containerEntity.setId(ContractBlacklistEntity.CONTAINER_ID);
        containerEntity.setChangeDate(LocalDate.now());

        contractBlacklistRepository.deleteById(ContractBlacklistEntity.CONTAINER_ID);
        contractBlacklistRepository.save(containerEntity);
    }
}

