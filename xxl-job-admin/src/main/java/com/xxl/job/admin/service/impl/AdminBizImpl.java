package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.thread.JobCompleteHelper;
import com.xxl.job.admin.core.thread.JobRegistryHelper;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.XxlJobInfoParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 实现AdminBiz接口，负责XXL-JOB后台服务的核心业务逻辑处理。
 * 包含任务调度、执行器注册与状态管理等功能。
 *
 * @author xuxueli 2017-07-27 21:54:20
 */
@Service
public class AdminBizImpl implements AdminBiz {


    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        // 调用助手类处理执行器回调的任务结果
        return JobCompleteHelper.getInstance().callback(callbackParamList);
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        // 注册执行器到服务端
        return JobRegistryHelper.getInstance().registry(registryParam);
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        // 从服务端移除已注册的执行器
        return JobRegistryHelper.getInstance().registryRemove(registryParam);
    }

    @Resource
    private XxlJobService xxlJobService;

    /**
     * 将XxlJobInfoParam转换为XxlJobInfo对象
     *
     * @param jobInfoParam 待转换的XxlJobInfoParam对象
     *
     * @return 转换后的XxlJobInfo对象
     */
    private XxlJobInfo convertXxlJobInfoParam(XxlJobInfoParam jobInfoParam) {
        XxlJobInfo target = new XxlJobInfo();
        // XxlJobInfoParam和XxlJobInfo有相同的属性，直接赋值
        BeanUtils.copyProperties(jobInfoParam, target);
        return target;
    }

    @Override
    public ReturnT<String> addXxlJob(XxlJobInfoParam jobInfo) {
        // 添加新任务
        return xxlJobService.add(convertXxlJobInfoParam(jobInfo));
    }

    @Override
    public ReturnT<String> updateXxlJob(XxlJobInfoParam jobInfo) {
        // 更新任务信息
        return xxlJobService.update(convertXxlJobInfoParam(jobInfo));
    }

    @Override
    public ReturnT<String> removeXxlJob(int id) {
        // 删除指定ID的任务
        return xxlJobService.remove(id);
    }

    @Override
    public ReturnT<String> startXxlJob(int id) {
        // 立即执行指定ID的任务
        return xxlJobService.start(id);
    }

    @Override
    public ReturnT<String> stopXxlJob(int id) {
        // 停止指定ID的任务
        return xxlJobService.stop(id);
    }
}
