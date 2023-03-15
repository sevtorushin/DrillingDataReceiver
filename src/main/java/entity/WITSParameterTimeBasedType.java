package entity;

public enum WITSParameterTimeBasedType {
    DATE("witsDate", "05"),
    TIME("witsTime", "06"),
    DBTM("bitDepth", "08"),
    DMEA("depth", "10"),
    BPOS("blockPosition", "12"),
    HKLA("hookLoad", "14"),
    SPPA("pressure", "21");

    private String name;
    private String code;

    WITSParameterTimeBasedType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
