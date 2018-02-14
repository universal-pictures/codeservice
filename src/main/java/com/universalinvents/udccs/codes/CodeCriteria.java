package com.universalinvents.udccs.codes;

public class CodeCriteria {
    private String key;
    private String operand;
    private Object value;

    public CodeCriteria(String key, String operand, Object value) {
        this.key = key;
        this.operand = operand;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
