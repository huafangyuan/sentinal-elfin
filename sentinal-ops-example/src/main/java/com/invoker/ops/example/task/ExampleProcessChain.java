package com.invoker.ops.example.task;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/02/27
 */
@Component
public class ExampleProcessChain extends ProcessorChain<FilterHandler> {

    @Resource
    private ExampleFilterHandler exampleFilterHandler;

    @Override
    public void execute(ContextAware contextAware) {
        for (FilterHandler filterHandler : this.chainList) {
            if (filterHandler.toSupport(contextAware)) {
                filterHandler.doHandler(contextAware);
            }
        }
    }

    @Override
    public void defaultHandlerCombination() {
        //TODO handler combination
        this.add(exampleFilterHandler);
    }
}
