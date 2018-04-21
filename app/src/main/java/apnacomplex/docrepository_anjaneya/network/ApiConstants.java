package apnacomplex.docrepository_anjaneya.network;

/**
 * Created by abhilash on 01-09-2016.
 */

public class ApiConstants {
    public interface ApiName {

        //--------------------API URL---------------------------------------------//

        public static final String BASE_API_URL = "https://s3.ap-south-1.amazonaws.com/mobileassignment/";    // production URL

        //-------------------APIs Constants---------------------------------------//
        public static final String DOCUMENT_CATEGORIES = BASE_API_URL + "repository/doc_categories";
        public static final String DOCUMENTS_LIST = BASE_API_URL + "repository/docs_list/";
    }

    public interface StandardHeader {
        String HEADER_KEY_CONTENT_TYPE = "Content-Type";
        String HEADER_VALUE_APPLICATION_JSON = "application/json";
//        String HEADER_KEY_ANDROID_ID = "Android-Id";
//        String HEADER_KEY_COMPRESS_OUTPUT = "Compress-Output";
//        String HEADER_KEY_REQUEST_ID = "Request-Id";
//        String HEADER_KEY_AUTHORIZATION = "Authorization";
//        String HEADER_KEY_CLIENT_TIME = "Client-Time";
//        String MULTIPART_FORM_DATA = "multipart/form-data";
    }

}
