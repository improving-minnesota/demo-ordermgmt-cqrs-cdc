package com.snacks.orderprocessor.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopologyController {
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Autowired
    public TopologyController(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }

    @GetMapping("/api/topology")
    public ResponseEntity<String> getTopology() {
        String topologyHtml = String.format("<html><body><pre>%s</pre></body></html>", streamsBuilderFactoryBean.getTopology().describe().toString());
        return ResponseEntity.ok(topologyHtml);
    }
}
