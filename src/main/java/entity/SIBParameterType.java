package entity;

public enum SIBParameterType {
    SHOCK(21, "Shock"),
    GTOT(31, "Gtot"),
    STSL(37, "StSl"),
    OMEGA(48, "Omega"),
    PACKAGE_NUMBER(66, "Package"),
    INCR(67, "IncR"),
    RPM(73, "RPM"),
    GAMMA(74, "Gamma"),
    GTF(71, "GTF"),
    MTF(72, "MTF"),
    TEMP(-114, "Temp"),
    GX(14, "Gx"),
    GY(15, "Gy"),
    GZ(16, "Gz"),
    BX(17, "Bx"),
    BY(18, "By"),
    BZ(19, "Bz"),
    DMT(29, "DMT"),
    INCD(67, "IncD"),
    AZD(68, "AzD");


    private int bytePerformance;
    private String name;

    SIBParameterType(int bytePerformance, String name) {
        this.bytePerformance = bytePerformance;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getBytePerformance() {
        return bytePerformance;
    }
}
