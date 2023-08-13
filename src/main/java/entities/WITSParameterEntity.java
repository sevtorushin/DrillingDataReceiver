package entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import utils.DateConverter;
import utils.DateTimeConverter;
import utils.TimeConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "WITSData" , schema = "sib")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WITSParameterEntity implements ParameterEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreationTimestamp
    @Convert(converter = DateTimeConverter.class)
    @Column(name = "local_date_time", nullable = false)
    private LocalDateTime localDateTime;

    @Basic
    @Convert(converter = DateConverter.class)
    @Column(name = "witsDate", nullable = false)
    private LocalDate witsDate;

    @Basic
    @Convert(converter = TimeConverter.class)
    @Column(name = "witsTime", nullable = false)
    private LocalTime witsTime;

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
        this.witsDate = witsDate;
        this.witsTime = witsTime;
        this.blockPosition = blockPosition;
        this.bitDepth = bitDepth;
        this.depth = depth;
        this.hookLoad = hookLoad;
        this.pressure = pressure;
    }
}
