package com.mutualfunds.backend.mutualfundapi.initializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mutualfunds.backend.mutualfundapi.constants.JsonConstants;
import com.mutualfunds.backend.mutualfundapi.pojo.entity.FundStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class StrategyInitializer implements CommandLineRunner {

    private final static String STRATEGIES_PATH = "src/main/resources/jsons/";

    private final static String FILE_NAME = "strategies.json";

    @Override
    public void run(String... args) throws Exception {
        List<FundStrategy> strategies = new ArrayList<>();
        try{
            strategies = JsonConstants.OBJECT_MAPPER.readValue(
                    new File(STRATEGIES_PATH + FILE_NAME),
                    new TypeReference<List<FundStrategy>>() {}
            );
        }catch (Exception e){
            log.error("Error while reading file '{}'. Message : {}", FILE_NAME, e.getMessage());
        }
    }
}
