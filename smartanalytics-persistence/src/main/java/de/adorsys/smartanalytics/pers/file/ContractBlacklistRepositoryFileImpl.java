package de.adorsys.smartanalytics.pers.file;

import de.adorsys.smartanalytics.api.config.ContractBlacklist;
import de.adorsys.smartanalytics.pers.api.ContractBlacklistEntity;
import de.adorsys.smartanalytics.pers.spi.ContractBlacklistRepositoryIf;
import de.adorsys.smartanalytics.pers.utils.ImportUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Profile({"file-persistence"})
@Service
public class ContractBlacklistRepositoryFileImpl implements ContractBlacklistRepositoryIf {

    private static final String CONTRACT_BLACKLIST_YML = "contract-blacklist.yml";

    @Value("${smartanalytics.conf.path:#{systemProperties['java.io.tmpdir']}}")
    private File confDir;

    private ContractBlacklistEntity contractBlacklistEntity;

    @PostConstruct
    public void postConstruct() throws IOException {
        File categoriesFile = new File(confDir, CONTRACT_BLACKLIST_YML);
        if (categoriesFile.exists()) {
            contractBlacklistEntity = ImportUtils.readAsYaml(categoriesFile, ContractBlacklistEntity.class);
        }
    }

    @Override
    public Optional<ContractBlacklistEntity> getContractBlacklist() {
        return Optional.ofNullable(contractBlacklistEntity);
    }

    @Override
    public void saveContractBlacklist(ContractBlacklist contractBlackList) {
        contractBlacklistEntity = new ContractBlacklistEntity();
        BeanUtils.copyProperties(contractBlackList, contractBlacklistEntity);

        try {
            ImportUtils.writeObjectToYaml(new File(confDir, CONTRACT_BLACKLIST_YML), contractBlacklistEntity);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

