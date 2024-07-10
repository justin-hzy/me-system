package com.me.modules.order.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QryMRetailPayItemRespDto {


    @TableField("c_payway_id")
    private Integer cPayWayId;

    @TableField("tot_amt_actual")
    private BigDecimal totAmtActual;
}
