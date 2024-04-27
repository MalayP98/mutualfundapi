package com.mutualfunds.backend.mutualfundapi.initializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mutualfunds.backend.mutualfundapi.constants.JsonConstants;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.FundStrategy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class StrategyInitializer implements CommandLineRunner {

    private final static String STRATEGIES_PATH = "src/main/resources/jsons/strategies.json";

    @Override
    public void run(String... args) throws Exception {
        List<FundStrategy> strategies = JsonConstants.OBJECT_MAPPER.readValue(
                new File(STRATEGIES_PATH),
                new TypeReference<List<FundStrategy>>() {}
        );
    }
}
