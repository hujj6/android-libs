package com.hujinwen.xhs;

import com.hujinwen.client.NanoServer;
import com.hujinwen.entity.annotation.nano.Router;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created by hu-jinwen on 12/17/20
 */
public class HTTPServer extends NanoServer {

    public HTTPServer(String hostname, int port) {
        super(hostname, port);
    }


    /**
     * 生成shield
     */
    @Router("/genShield")
    public Response genShield(IHTTPSession session) {
        Map<String, List<String>> postData = parsePostData(session);
        if (!postData.containsKey("url") || !postData.containsKey("xy_common_params")) {
            return Response.newFixedLengthResponse("Miss params!");
        }
        final List<String> urlList = postData.get("url");
        final List<String> paramList = postData.get("xy_common_params");
        final List<String> postBodyList = postData.get("postBody");

        // 参数完整性过滤
        if (urlList == null || paramList == null || urlList.isEmpty() || paramList.isEmpty()) {
            return Response.newFixedLengthResponse("Miss params!");
        }

        final String url = urlList.get(0);
        final String xy_common_params = paramList.get(0);

        if (postBodyList != null && !postBodyList.isEmpty()) {
            // post加密
            final String postBodyStr = paramList.get(0);
            return Response.newFixedLengthResponse(Searcher.genShieldPost(url, xy_common_params, postBodyStr));
        }
        return Response.newFixedLengthResponse(Searcher.genShieldGet(url, xy_common_params));
    }

    /**
     * 健康检查
     */
    @Router("/status")
    public Response healthCheck(IHTTPSession session) {
        return Response.newFixedLengthResponse("OK");
    }

    public static void startServer() throws IOException {
        new HTTPServer("0.0.0.0", 8848).start();
    }

}
