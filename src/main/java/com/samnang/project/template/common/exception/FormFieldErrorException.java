package com.samnang.project.template.common.exception;


import lombok.Data;

import java.util.HashMap;

@Data
public class FormFieldErrorException extends Exception {

    private HashMap<String, String> fieldMessage = new HashMap<>();

    public HashMap<String, String> getFieldMessage() {
        return this.fieldMessage;
    }

    public FormFieldErrorException(String message, HashMap<String, String> fieldMessage) {
        super(message);
        this.fieldMessage = fieldMessage;
    }

}