package com.zhangw.aliencat.ui.test.volloy;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * @author zhangw
 * @date 2018/4/10.
 * 描述 gsonrequest
 */
public class GsonRequest<T> extends Request<T> {
    private Response.Listener<T> mListener;
    private Class<T> clz;
    private Gson gson;

    public GsonRequest(String url, Class<T> clz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, clz, listener, errorListener);
    }

    public GsonRequest(int method, String url, Class<T> clz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        this.clz = clz;
        gson = new Gson();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            parsed = new String(parsed.getBytes( "UTF-8"));
            return Response.success(gson.fromJson(parsed,clz),HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError());
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
