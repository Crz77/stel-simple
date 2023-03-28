package domain;

import domain.helpers.MetricNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@DiscriminatorValue("MetricData")
public class Metric extends TelemetryData {

    private static final long serialVersionUID = 1L;
    private String metricName;

    @ElementCollection
    @ToString.Exclude
    private List<MetricNode> values = new ArrayList<>();

    @OneToOne
    @ToString.Exclude
    private Detector detector;

    public Metric(Long senderID, String name, LocalDateTime timestamp, String commonName, boolean isLogMessage,
                  String metricName) {
        super(senderID, name, timestamp, commonName, isLogMessage);

        this.metricName = metricName;
    }

    public void addMetricNode(MetricNode node){
        if(node == null)
            throw new IllegalArgumentException("Node is null");

        values.add(node);
        if(detector != null)
            detector.setLastNode(node.getTimeStamp());
    }

    public void removeMetricNode(MetricNode node){
        if(node == null)
            throw new IllegalArgumentException("Node is null");

        values.remove(node);
    }

}
