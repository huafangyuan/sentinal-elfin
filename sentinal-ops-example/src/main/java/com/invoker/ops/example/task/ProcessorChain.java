package com.invoker.ops.example.task;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/02/27
 */
public abstract class ProcessorChain<T> {

    public List<T> chainList = Lists.newArrayList();

    public <P extends ProcessorChain<T>> ProcessorChain<T> add(T t) {
        chainList.add(t);
        return this;
    }

    public void process(ContextAware contextAware) {
        if (CollectionUtils.isEmpty(chainList)) {
            defaultHandlerCombination();
        }
        this.execute(contextAware);

    }

    /**
     * @param contextAware
     */
    public abstract void execute(ContextAware contextAware);

    /**
     *
     */
    public abstract void defaultHandlerCombination();
}
