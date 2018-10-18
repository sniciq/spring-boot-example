package com.ac.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AppInitService {

    private Logger logger = LoggerFactory.getLogger(AppInitService.class);

    public void init() {
        logger.info("app init...");


        logger.info("app init...over");
    }
}
