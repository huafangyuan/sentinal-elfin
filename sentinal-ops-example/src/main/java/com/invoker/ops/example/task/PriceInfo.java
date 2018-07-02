package com.invoker.ops.example.task;

import lombok.Data;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/03/14
 */
@Data
public class PriceInfo {

    /**
     * 车型
     */
    private Long carType;

    /**
     * 价格
     */
    private String price;

}
