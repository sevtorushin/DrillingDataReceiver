package entity;

import java.io.Serializable;

public class SIBParameter implements Serializable {
    private String parameterName;
    private double parameterData;
    private int quality;

    public SIBParameter(String parameterName, double parameterData, int quality) {
        this.parameterName = parameterName;
        this.parameterData = parameterData;
        this.quality = quality;
    }

    public SIBParameter() {
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public double getParameterData() {
        return parameterData;
    }

    public void setParameterData(double parameterData) {
        this.parameterData = parameterData;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "SIBParameter{" +
                "parameterName='" + parameterName + '\'' +
                ", parameterData=" + parameterData +
                ", quality=" + quality +
                '}';
    }
}
