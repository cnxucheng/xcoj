package com.github.cnxucheng.xcojModel.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestCase implements Serializable {

    private String input;

    private String output;

}
