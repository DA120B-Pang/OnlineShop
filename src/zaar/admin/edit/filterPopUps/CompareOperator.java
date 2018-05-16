package zaar.admin.edit.filterPopUps;

public enum CompareOperator {
    LESS("Less than"),
    EQUAL("Equal to"),
    GREATER("Greater than");

    private String value;
    CompareOperator(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
