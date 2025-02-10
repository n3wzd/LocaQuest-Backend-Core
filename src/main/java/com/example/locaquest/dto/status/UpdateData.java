package com.example.locaquest.dto.status;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateData {
    private int maxLevel;
    private List<Integer> expLimitList;
}
