package apnacomplex.docrepository_anjaneya.network;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import apnacomplex.docrepository_anjaneya.R;

/**
 * Created by Ruthvik on 01-09-2016.
 */

public class DocRepositoryRequest extends Request<DocRepositoryResponse> {

    private final Context mContext;

    private final String mUrl;

    private Response.Listener<DocRepositoryResponse> mListener;

    private HashMap<String, String> mHeaders;

    private String mRequestBody;


    /**
     * Constructor used to create network request
     *
     * @param context          TRBApplication context
     * @param method           com.android.volley.Request.Method
     * @param api              Api Name
     * @param responseListener Listener to get response.
     * @param requestBody      Request body if any
     */

    public DocRepositoryRequest(Context context, int method, String api, Response.Listener<DocRepositoryResponse>
            responseListener, String requestBody) {
        super(method, api, null);
        mUrl = api;
        mContext = context;
        mListener = responseListener;
        mHeaders = new HashMap<String, String>();
        mRequestBody = requestBody;
//        addDefaultHeaders();
//        setTag(getRequestId());
    }

    @Override
    protected Response<DocRepositoryResponse> parseNetworkResponse(NetworkResponse response) {
        DocRepositoryResponse docRepositoryResponse = new DocRepositoryResponse();
        docRepositoryResponse.setIsSuccess(true);
        docRepositoryResponse.setHeaders(response.headers);
        try {
            docRepositoryResponse.setResponseBody(new String(response.data, HttpHeaderParser.parseCharset
                    (response.headers)));
        } catch (UnsupportedEncodingException e) {
            docRepositoryResponse.setResponseBody(new String(response.data));
        }
        return Response.success(docRepositoryResponse, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(DocRepositoryResponse response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        DocRepositoryResponse docRepositoryResponse = new DocRepositoryResponse();
        docRepositoryResponse.setIsSuccess(false);
        int statusCode = 0;
        if (error != null && error.networkResponse != null) {
            statusCode = error.networkResponse.statusCode;
            docRepositoryResponse.setHeaders(error.networkResponse.headers);
            if (!TextUtils.isEmpty(error.getMessage())) {
                docRepositoryResponse.setErrorMessage(error.getMessage());
            }
        }
        if (TextUtils.isEmpty(docRepositoryResponse.getErrorMessage())) {
            docRepositoryResponse.setErrorMessage(getErrorMessageForCode(statusCode, docRepositoryResponse));
        }
        mListener.onResponse(docRepositoryResponse);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders;
    }

    @Override
    public String getBodyContentType() {
        return ApiConstants.StandardHeader.HEADER_VALUE_APPLICATION_JSON;
    }


    @Override
    public byte[] getBody() throws AuthFailureError {
        return mRequestBody == null ? null : mRequestBody.getBytes();
    }

    public void addHeader(String key, String value) {
        mHeaders.put(key, value);
    }

    private String getErrorMessageForCode(int statusCode, DocRepositoryResponse response) {
        String errorMsg = mContext.getResources().getString(R.string.deafult_error);
        switch (statusCode) {
            case 400:
                errorMsg = mContext.getResources().getString(R.string.bad_request);
                break;
            case 401:
                errorMsg = mContext.getResources().getString(R.string.Forbidden);
                break;
            case 403:
                errorMsg = mContext.getResources().getString(R.string.Forbidden);
                break;
            case 500:
                errorMsg = mContext.getResources().getString(R.string.Internal_Server_Error);
                break;
            case 502:
                errorMsg = mContext.getResources().getString(R.string.Bad_Gateway);
                break;
            case 503:
                errorMsg = mContext.getResources().getString(R.string.deafult_error);
                break;
            case 504:
                errorMsg = mContext.getResources().getString(R.string.deafult_error);
                break;
            case 404:
                errorMsg = mContext.getResources().getString(R.string.No_Network);
        }
        return errorMsg;
    }

    protected void addDefaultHeaders() {
        addHeader(ApiConstants.StandardHeader.HEADER_KEY_CONTENT_TYPE, ApiConstants
                .StandardHeader.HEADER_VALUE_APPLICATION_JSON);

//        addHeader(ApiConstants.StandardHeader.HEADER_KEY_CLIENT_TIME, DateTimeUtil
//                .getApiDateFormat(Calendar.getInstance().getTime()));

    }

    public String getRequestBody() {
        return mRequestBody;
    }

    public String getUrl() {
        return mUrl;
    }
}
