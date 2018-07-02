package com.invoker.ops.example.task;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/02/27
 */
public interface BizFuncFactory extends FuncFactory {

    BizFunc create(BizFunc next);
}
