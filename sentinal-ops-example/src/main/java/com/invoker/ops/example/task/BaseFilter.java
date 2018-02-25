package com.invoker.ops.example.task;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/02/25
 */
public abstract class BaseFilter<RQ extends BaseRequest, RS extends BaseResponse> implements Cloneable {


    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public abstract boolean dealObject(RQ request, RS response) throws Exception;

    public boolean execute(RQ request, RS response) {
        try {
            boolean result = this.dealObject(request, response);
            return result;
        } catch (Exception e) {

        }
        return true;
    }

}
