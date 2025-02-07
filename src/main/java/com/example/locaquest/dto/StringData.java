package com.example.locaquest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringData {
    private String data;

    public StringData() {
        
    }

    public StringData(String data) {
        this.data = data;
    }
}
