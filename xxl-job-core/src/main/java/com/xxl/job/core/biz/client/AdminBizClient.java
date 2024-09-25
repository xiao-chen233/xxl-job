package com.xxl.job.core.biz.client;

import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.XxlJobInfoParam;
import com.xxl.job.core.util.XxlJobRemotingUtil;

import java.util.HashMap;
import java.util.List;

/**
 * AdminBiz接口的客户端实现，面向执行器或其他客户端应用，
 * 提供远程调用服务端API的功能，以执行任务调度管理操作。
 * <p>
 * 注意：构造函数允许自定义访问地址、访问令牌及超时时间。
 *
 * @author xuxueli 2017-07-28 22:14:52
 */
public class AdminBizClient implements AdminBiz {
    private String addressUrl; // 服务端地址
    private String accessToken; // 访问令牌
    private int timeout = 3; // 默认请求超时时间（秒）

    public AdminBizClient() {
    }

    public AdminBizClient(String addressUrl, String accessToken) {
        this.addressUrl = addressUrl;
        this.accessToken = accessToken;

        // valid
        if (!this.addressUrl.endsWith("/")) {
            this.addressUrl = this.addressUrl + "/";
        }
    }

    public AdminBizClient(String addressUrl, String accessToken, int timeout) {
        this.addressUrl = addressUrl;
        this.accessToken = accessToken;
        // valid
        if (!this.addressUrl.endsWith("/")) {
            this.addressUrl = this.addressUrl + "/";
        }
        this.timeout = timeout;
    }

    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        return XxlJobRemotingUtil.postBody(addressUrl + "api/callback", accessToken, timeout, callbackParamList, String.class);
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        return XxlJobRemotingUtil.postBody(addressUrl + "api/registry", accessToken, timeout, registryParam, String.class);
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        return XxlJobRemotingUtil.postBody(addressUrl + "api/registryRemove", accessToken, timeout, registryParam, String.class);
    }

    @Override
    public ReturnT<String> addXxlJob(XxlJobInfoParam jobInfo) {
        return XxlJobRemotingUtil.postBody(addressUrl + "api/addXxlJob", accessToken, timeout, jobInfo, String.class);
    }

    @Override
    public ReturnT<String> updateXxlJob(XxlJobInfoParam jobInfo) {
        return XxlJobRemotingUtil.postBody(addressUrl + "api/updateXxlJob", accessToken, timeout, jobInfo, String.class);
    }

    @Override
    public ReturnT<String> removeXxlJob(int id) {
        HashMap<String, Object> requestObj = new HashMap<>();
        requestObj.put("id", id);
        return XxlJobRemotingUtil.postBody(addressUrl + "api/removeXxlJob", accessToken, timeout, requestObj, String.class);
    }

    @Override
    public ReturnT<String> startXxlJob(int id) {
        HashMap<String, Object> requestObj = new HashMap<>();
        requestObj.put("id", id);
        return XxlJobRemotingUtil.postBody(addressUrl + "api/startXxlJob", accessToken, timeout, requestObj, String.class);
    }

    @Override
    public ReturnT<String> stopXxlJob(int id) {
        HashMap<String, Object> requestObj = new HashMap<>();
        requestObj.put("id", id);
        return XxlJobRemotingUtil.postBody(addressUrl + "api/stopXxlJob", accessToken, timeout, requestObj, String.class);
    }
}
