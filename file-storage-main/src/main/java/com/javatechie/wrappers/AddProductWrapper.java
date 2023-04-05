package com.javatechie.wrappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class AddProductWrapper {
    private String name;

    private double price;

    private String description;

    private String path;
}
