package com.invoker.ops.example.task;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/02/25
 */
public abstract class BaseTask<RQ extends BaseRequest, RS extends BaseResponse> {

    @Setter
    @Getter
    private List<BaseFilter<RQ, RS>> filters;

    /**
     * @param request
     * @param response
     * @throws Exception
     */
    protected abstract void beforeExecute(RQ request, RS response) throws Exception;

    /**
     * @param request
     * @param response
     * @throws Exception
     */
    protected abstract void afterExecute(RQ request, RS response) throws Exception;

    public void execute(RQ request, RS response) {
        try {
            this.beforeExecute(request, response);
            Assert.notEmpty(filters, "processTask.filters must not empty");
            for (BaseFilter filter : filters) {
                boolean result = filter.execute(request, response);
                if (!result) {
                    break;
                }
            }
            this.afterExecute(request, response);
        } catch (Exception e) {

        }
    }


}
