package com.invoker.ops.example.test;

import com.invoker.ops.example.task.BaseTask;
import com.invoker.ops.example.task.TaskFilters;

import javax.xml.ws.Response;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/02/26
 */
@TaskFilters("exampleTask.stepOneFilter,exampleTask.stepTwoFilter")
public class ExampleTask extends BaseTask<ERequest, EResponse> {


    @Override
    protected void beforeExecute(ERequest request, EResponse response) throws Exception {

    }

    @Override
    protected void afterExecute(ERequest request, EResponse response) throws Exception {

    }
}
