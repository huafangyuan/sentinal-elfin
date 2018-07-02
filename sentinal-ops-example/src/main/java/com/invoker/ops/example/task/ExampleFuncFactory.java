package com.invoker.ops.example.task;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/02/27
 */
public class ExampleFuncFactory extends AbstractFuncFactory {

    @Override
    protected BaseResponse apply(BizFunc next, FuncName funcName, BaseRequest req, Object[] args) {
        try {
            //链式调用
            next.apply(funcName, req, args);
        } catch (Exception e) {

        }
        return null;
    }
}
