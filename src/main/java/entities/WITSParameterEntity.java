package entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import utils.DateConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "WITSData" , schema = "sib")
@Data
@NoArgsConstructor
public class WITSParameterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreationTimestamp
    @Convert(converter = DateConverter.class)
    @Column(name = "local_date_time", nullable = false)
    private LocalDateTime localDateTime;

    @Basic
    @Column(name = "witsDate", nullable = false)
    private String witsDate;

    @Basic
    @Column(name = "witsTime", nullable = false)
    private String witsTime;

    @Basic
    @Column(name = "blockPosition", nullable = false)
    private double blockPosition;

    @Basic
    @Column(name = "bitDepth", nullable = false)
    private double bitDepth;

    @Basic
    @Column(name = "depth", nullable = false)
    private double depth;

    @Basic
    @Column(name = "hookLoad", nullable = false)
    private double hookLoad;

    @Basic
    @Column(name = "pressure", nullable = false)
    private double pressure;

    public WITSParameterEntity(LocalDate witsDate, LocalTime witsTime,
                               double blockPosition, double bitDepth, double depth,
                               double hookLoad, double pressure) {
        this.witsDate = witsDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.witsTime = witsTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
        this.blockPosition = blockPosition;
        this.bitDepth = bitDepth;
        this.depth = depth;
        this.hookLoad = hookLoad;
        this.pressure = pressure;
    }
}
