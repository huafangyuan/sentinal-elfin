package com.invoker.ops.example.task;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/02/27
 */
public interface BizFunc<T extends FuncName> {

    BaseResponse apply(T apiName, BaseRequest req, Object[] args);
}
