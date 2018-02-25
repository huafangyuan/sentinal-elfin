package com.invoker.ops.example.task;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * Description: 责任链模式
 *
 * @author fangyuan.lw
 * @date 2018/02/25
 */
public class TaskFrameProcessor extends InitDestroyAnnotationBeanPostProcessor implements ApplicationContextAware {

    public static final Logger logger = LoggerFactory.getLogger(TaskFrameProcessor.class);

    private ApplicationContext applicationContext;

    /**
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try {
            if (bean instanceof BaseTask) {
                this.processTaskChain((BaseTask) bean, beanName);
            }
        } catch (Exception e) {
            throw new BeanInitializationException("TaskFrameProcessor.processTaskChain exception" + beanName, e);
        }
        return bean;
    }

    /**
     * @param baseTask
     * @param beanName
     * @throws Exception
     */
    private void processTaskChain(BaseTask baseTask, String beanName) throws Exception {
        Class<?> clazz = this.getBeanClass(baseTask);
        TaskFilters anno = null;
        while (null == anno && clazz != null) {
            anno = clazz.getAnnotation(TaskFilters.class);
            clazz = clazz.getSuperclass();
        }
        if (null == anno) {
            logger.error("TaskFrameProcessor.processTaskChain.taskFilters is blank beanName{}", beanName);
            return;
        }
        List<BaseFilter> filters = Lists.newArrayList();
        List<String> taskFilters = Lists.newArrayList(Splitter.on(",").trimResults().split(anno.value()));
        for (String filter : taskFilters) {
            BaseFilter baseFilter = (BaseFilter) this.applicationContext.getBean(filter);
            if (null != baseFilter) {
                filters.add(baseFilter);
            }
        }
        baseTask.setFilters(filters);
    }

    /**
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @param bean
     * @return
     * @throws Exception
     */
    private Class<?> getBeanClass(Object bean) throws Exception {
        Class clazz;
        if (AopUtils.isJdkDynamicProxy(bean)) {
            clazz = ((Advised) bean).getTargetSource().getTarget().getClass();
        } else {
            clazz = bean.getClass();
        }
        return clazz;
    }
}
