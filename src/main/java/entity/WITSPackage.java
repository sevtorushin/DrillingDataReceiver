package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class WITSPackage implements Serializable {
    private LocalDate witsDate;
    private LocalTime witsTime;

    public WITSPackage(LocalDate witsDate, LocalTime witsTime) {
        this.witsDate = witsDate;
        this.witsTime = witsTime;
    }

    public WITSPackage() {
    }
}
