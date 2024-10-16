package com.local_dating.producer.presentation;

import com.local_dating.producer.application.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @PostMapping(value = "/producer")
    public void producer(@RequestBody final String data) {
        System.out.println("호출");
        producerService.sendMessage("my-topic", data);
        //return "";
    }
}
