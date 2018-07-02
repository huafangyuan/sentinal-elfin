package com.invoker.ops.example.task;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/02/27
 */
public interface FilterHandler {

    void doHandler(ContextAware contextAware);

    boolean toSupport(ContextAware contextAware);
}
