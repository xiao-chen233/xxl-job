package com.xxl.job.admin.controller;

import com.xxl.job.admin.controller.annotation.PermissionLimit;
import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.XxlJobInfoParam;
import com.xxl.job.core.util.GsonTool;
import com.xxl.job.core.util.XxlJobRemotingUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * JobApiController 类负责处理与作业（Job）相关的API请求。
 * 这个控制器作为前端和后端交互的接口，提供了执行各种操作的功能，
 * 如注册执行器、回调任务处理结果、获取作业信息等。
 *
 * @author xuxueli 2017/5/10
 */
@Controller
@RequestMapping("/api")
public class JobApiController {

    @Resource
    private AdminBiz adminBiz;

    /**
     * 处理API请求的核心方法。
     * 根据传入的uri路径，分发到不同的业务逻辑处理。
     * 支持接收POST请求中的JSON数据，执行相应操作，并返回处理结果。
     *
     * @param request HTTP请求对象，可用于获取请求相关的上下文信息。
     * @param uri     请求的URI路径，用于决定调用哪个具体的业务逻辑方法。
     * @param data    POST请求体中的数据，通常是JSON格式的字符串，根据不同的uri代表不同业务的数据。
     *                可能为空，具体取决于调用的API是否需要请求体数据。
     *
     * @return ReturnT<String> API调用的结果封装，包含一个code码指示操作状态，
     * 和一个msg消息提供更详细的描述或错误信息。如果操作成功，data字段可能携带额外的响应数据。
     */
    @RequestMapping("/{uri}")
    @ResponseBody
    @PermissionLimit(limit = false)
    public ReturnT<String> api(HttpServletRequest request, @PathVariable("uri") String uri, @RequestBody(required = false) String data) {

        // valid
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, HttpMethod not support.");
        }
        if (uri == null || uri.trim().isEmpty()) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, uri-mapping empty.");
        }
        if (XxlJobAdminConfig.getAdminConfig().getAccessToken() != null
                && !XxlJobAdminConfig.getAdminConfig().getAccessToken().trim().isEmpty()
                && !XxlJobAdminConfig.getAdminConfig().getAccessToken().equals(request.getHeader(XxlJobRemotingUtil.XXL_JOB_ACCESS_TOKEN))) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "The access token is wrong.");
        }

        // services mapping
        switch (uri) {
            case "callback":
                List<HandleCallbackParam> callbackParamList = GsonTool.fromJsonList(data, HandleCallbackParam.class);
                return adminBiz.callback(callbackParamList);
            case "registry": {
                RegistryParam registryParam = GsonTool.fromJson(data, RegistryParam.class);
                return adminBiz.registry(registryParam);
            }
            case "registryRemove": {
                RegistryParam registryParam = GsonTool.fromJson(data, RegistryParam.class);
                return adminBiz.registryRemove(registryParam);
            }
            case "addXxlJob": {
                XxlJobInfoParam jobInfo = GsonTool.fromJson(data, XxlJobInfoParam.class);
                return adminBiz.addXxlJob(jobInfo);
            }
            case "updateXxlJob": {
                XxlJobInfoParam jobInfo = GsonTool.fromJson(data, XxlJobInfoParam.class);
                return adminBiz.updateXxlJob(jobInfo);
            }
            case "removeXxlJob": {
                Map<String, Object> dataMap = GsonTool.fromJsonMap(data, String.class, Object.class);
                int id = (int) dataMap.get("id");
                return adminBiz.removeXxlJob(id);
            }
            case "startXxlJob": {
                Map<String, Object> dataMap = GsonTool.fromJsonMap(data, String.class, Object.class);
                int id = (int) dataMap.get("id");
                return adminBiz.startXxlJob(id);
            }
            case "stopXxlJob": {
                Map<String, Object> dataMap = GsonTool.fromJsonMap(data, String.class, Object.class);
                int id = (int) dataMap.get("id");
                return adminBiz.stopXxlJob(id);
            }
            default:
                return new ReturnT<String>(ReturnT.FAIL_CODE, "invalid request, uri-mapping(" + uri + ") not found.");
        }

    }

}
