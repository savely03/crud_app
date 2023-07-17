package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

@RestController
@RequestMapping("/info")
public class InfoController {

    private final Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Value("${server.port}")
    private String port;

    @GetMapping
    public String getPort() {
        return port;
    }

    @GetMapping("/number")
    public int getNumberByFormula() {
        long startTime = System.currentTimeMillis();
        int number = IntStream.range(0, 1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
        logger.info("Method execution time - {}", System.currentTimeMillis() - startTime);
        return number;
    }
}
