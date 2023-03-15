package entity;

import annotations.WITSPackageCode;
import annotations.WITSRecordCode;

import java.time.LocalDate;
import java.time.LocalTime;

@WITSPackageCode(code = "01")
public class WITSPackageTimeBased extends WITSPackage {
    @WITSRecordCode(code = "05")
    private LocalDate witsDate;
    @WITSRecordCode(code = "06")
    private LocalTime witsTime;
    @WITSRecordCode(code = "12")
    private double blockPosition;
    @WITSRecordCode(code = "08")
    private double bitDepth;
    @WITSRecordCode(code = "10")
    private double depth;
    @WITSRecordCode(code = "14")
    private double hookLoad;
    @WITSRecordCode(code = "21")
    private double pressure;

    public WITSPackageTimeBased(LocalDate witsDate, LocalTime witsTime, double blockPosition, double bitDepth, double depth, double hookLoad, double pressure) {
        super(witsDate, witsTime);
        this.witsDate = witsDate;
        this.witsTime = witsTime;
        this.blockPosition = blockPosition;
        this.bitDepth = bitDepth;
        this.depth = depth;
        this.hookLoad = hookLoad;
        this.pressure = pressure;
    }

    public WITSPackageTimeBased() {
    }

    public LocalDate getWitsDate() {
        return witsDate;
    }

    public void setWitsDate(LocalDate witsDate) {
        this.witsDate = witsDate;
    }

    public LocalTime getWitsTime() {
        return witsTime;
    }

    public void setWitsTime(LocalTime witsTime) {
        this.witsTime = witsTime;
    }

    public double getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(double blockPosition) {
        this.blockPosition = blockPosition;
    }

    public double getBitDepth() {
        return bitDepth;
    }

    public void setBitDepth(double bitDepth) {
        this.bitDepth = bitDepth;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getHookLoad() {
        return hookLoad;
    }

    public void setHookLoad(double hookLoad) {
        this.hookLoad = hookLoad;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "WITSPackageTimeBased{" +
                "date=" + witsDate +
                ", time=" + witsTime +
                ", blockPosition=" + blockPosition +
                ", bitDepth=" + bitDepth +
                ", depth=" + depth +
                ", hookLoad=" + hookLoad +
                ", pressure=" + pressure +
                '}';
    }
}
