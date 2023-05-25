package com.samnang.project.template.configs;

import com.samnang.project.template.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author: yh
 */
@Component
public class ApplicationRunnerConfig implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRunnerConfig.class);

    @Autowired
    protected RedisUtil redisUtil;

    @Value("${spring.application.name}")
    protected String applicationName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        redisUtil.setValue(applicationName, applicationName, 2);
        LOGGER.info(applicationName + " run success ");
    }

}