package zowe.client.sdk.zosfiles.uss.types;

public enum ChangeTagType {
    BINARY("binary"),
    MIXED("mixed"),
    TEXT("text");

    private final String value;

    ChangeTagType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
