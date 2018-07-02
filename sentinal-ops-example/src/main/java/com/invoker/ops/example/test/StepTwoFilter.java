package com.invoker.ops.example.test;

import com.invoker.ops.example.task.BaseFilter;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/02/26
 */
@Component("exampleTask.stepTwoFilter")
public class StepTwoFilter extends BaseFilter<ERequest, EResponse> {

    @Override
    public boolean dealObject(ERequest request, EResponse response) throws Exception {
        return false;
    }
}
