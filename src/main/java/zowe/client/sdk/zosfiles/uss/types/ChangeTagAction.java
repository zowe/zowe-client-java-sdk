package zowe.client.sdk.zosfiles.uss.types;

public enum ChangeTagAction {
    SET("set"),
    REMOVE("remove"),
    list("list");

    private final String value;

    ChangeTagAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
