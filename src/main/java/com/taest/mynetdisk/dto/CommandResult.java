package com.taest.mynetdisk.dto;

import lombok.Data;

@Data
public class CommandResult {

    private boolean success = true;
    private String result;
    private String err;

}
