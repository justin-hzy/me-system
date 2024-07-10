package com.me.modules.fl.dto;


import com.me.modules.fl.pojo.Product;
import com.me.modules.fl.pojo.Shipping;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetOrderCBDto {

    private String name;

    private String status;

    private String internal_name;

    List<Shipping> shippings = new ArrayList<>();

    List<Product> products = new ArrayList<>();

}
