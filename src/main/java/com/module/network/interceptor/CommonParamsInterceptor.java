package com.module.network.interceptor;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求拦截器 - 添加 Body 参数
 */
public class CommonParamsInterceptor implements Interceptor {

    private Map<String, String> commonParams;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if (request.body() instanceof FormBody) {
            FormBody oldBody = (FormBody) request.body();
            FormBody.Builder newBodyBuilder = new FormBody.Builder();
            for (int i = 0; i < oldBody.size(); i++) {
                newBodyBuilder.addEncoded(oldBody.encodedName(i), oldBody.encodedValue(i));
            }
            for (Map.Entry<String, String> entry : commonParams.entrySet()) {
                newBodyBuilder.add(entry.getKey(), entry.getValue());
            }
            builder.method(request.method(), newBodyBuilder.build());
        }
        return chain.proceed(builder.build());
    }

    public void setCommonParams(Map<String, String> commonParams) {
        this.commonParams = commonParams;
    }
}
