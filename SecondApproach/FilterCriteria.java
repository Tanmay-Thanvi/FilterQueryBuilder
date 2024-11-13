package SecondApproach;

public class FilterCriteria {
    private String key;
    private Operator operator;
    private Object value;
    
    public FilterCriteria(String key, Operator operator, Object value) {
        this.key = key;
        this.operator = operator;
        this.value = value;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public Operator getOperator() {
        return operator;
    }
    public void setOperator(Operator operator) {
        this.operator = operator;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        return "%s %s %s".formatted(key,operator,value);
    }
}
