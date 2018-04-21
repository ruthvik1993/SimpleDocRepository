package apnacomplex.docrepository_anjaneya.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;


import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import apnacomplex.docrepository_anjaneya.BuildConfig;
import apnacomplex.docrepository_anjaneya.R;
import apnacomplex.docrepository_anjaneya.adapters.DocsListAdapter;
import apnacomplex.docrepository_anjaneya.adapters.SimpleDividerItemDecoration;
import apnacomplex.docrepository_anjaneya.dialog.NetworkError_Dialog;
import apnacomplex.docrepository_anjaneya.model.DocsListModel;
import apnacomplex.docrepository_anjaneya.network.ApiConstants;
import apnacomplex.docrepository_anjaneya.network.DocRepositoryRequest;
import apnacomplex.docrepository_anjaneya.network.DocRepositoryResponse;
import apnacomplex.docrepository_anjaneya.network.NetworkManager;
import apnacomplex.docrepository_anjaneya.network.NetworkUtil;
import apnacomplex.docrepository_anjaneya.util.AppConstants;
import apnacomplex.docrepository_anjaneya.util.AppUtil;

import static android.R.attr.permission;

public class DocListActivity extends AppCompatActivity {

    private String cat_id;
    private ImageView imgSort;
    private RecyclerView rvDocList;
    private Toolbar doc_list_toolbar;
    private Context mContext;
    private FloatingActionButton fab;
    private ArrayList<DocsListModel.DocumentsModel> documentsList;
    private RelativeLayout relativeSort;
    private boolean isAscending = false;
    private DocsListAdapter docCatAdapter;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1111;
    private String fileName;
    private long downloadReference;
    private boolean isDownloading = false;
    NetworkError_Dialog networkError_dialog;
    private boolean isTimer = false;
    Timer timer = new Timer();
    private String doc_uri;
    private String cat_name = "";
    public ProgressDialog pd = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);
        init();
        onClicks();
        if (getIntent().getExtras() != null) {
            cat_id = getIntent().getExtras().getString(AppConstants.CATEGORY_ID);
            cat_name = getIntent().getExtras().getString(AppConstants.CATEGORY_NAME);
        }
        setMyActionBar(cat_name);
        getDocsList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onClicks() {

        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAscending) {
                    sortDescending();
                    docCatAdapter.notifyDataSetChanged();
                    imgSort.animate().rotation(360);
                } else {
                    sortAscending();
                    docCatAdapter.notifyDataSetChanged();
                    imgSort.animate().rotation(180);
                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(rvDocList, "Document uploads not allowed for this user", Snackbar.LENGTH_SHORT);

                snackbar.show();
            }
        });

    }


    private void sortAscending() {
        Collections.sort(documentsList, new Comparator<DocsListModel.DocumentsModel>() {
            public int compare(DocsListModel.DocumentsModel v1, DocsListModel.DocumentsModel v2) {
                return v1.getDoc_name().compareTo(v2.getDoc_name());
            }
        });
        isAscending = true;
    }

    private void sortDescending() {
        Collections.sort(documentsList, new Comparator<DocsListModel.DocumentsModel>() {
            public int compare(DocsListModel.DocumentsModel v1, DocsListModel.DocumentsModel v2) {
                return v2.getDoc_name().compareTo(v1.getDoc_name());
            }
        });
        isAscending = false;
    }

    private void setMyActionBar(String catName) {
        setSupportActionBar(doc_list_toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_ic);
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'> " + catName + "</font>"));
        }
    }

    private void getDocsList() {
        pd = AppUtil.showProgressBar(mContext, getResources().getString(R.string.progress_message));
        DocRepositoryRequest docRepositoryRequest = new DocRepositoryRequest(mContext, DocRepositoryRequest.Method.GET,
                ApiConstants.ApiName.DOCUMENTS_LIST + cat_id, new Response.Listener<DocRepositoryResponse>() {
            @Override
            public void onResponse(DocRepositoryResponse response) {
                AppUtil.dismissProgressBar(pd);
                if (response.isSuccess()) {
                    Gson gson = new Gson();
                    DocsListModel docsListModel = gson.fromJson(response.getResponseBody().toString(), DocsListModel.class);
                    documentsList = docsListModel.getDocuments();
                    setAdapter();
                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {
                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                    networkError_dialog.showDialog(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            networkError_dialog.dismissDialog();
                            getDocsList();
                        }
                    });
                } else {
                    Snackbar snackbar = Snackbar
                            .make(rvDocList, AppConstants.RESPONSEFAILED, Snackbar.LENGTH_SHORT);

                    snackbar.show();
                }
            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(docRepositoryRequest);
    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvDocList.setLayoutManager(layoutManager);
        if (documentsList != null) {
            docCatAdapter = new DocsListAdapter(mContext, documentsList);
            rvDocList.addItemDecoration(new SimpleDividerItemDecoration(this));
            rvDocList.setAdapter(docCatAdapter);
        }
    }


    private void init() {
        mContext = DocListActivity.this;
        networkError_dialog = new NetworkError_Dialog(mContext);

        imgSort = (ImageView) findViewById(R.id.imgSort);
        doc_list_toolbar = (Toolbar) findViewById(R.id.doc_list_toolbar);
        rvDocList = (RecyclerView) findViewById(R.id.rvDocList);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        relativeSort = (RelativeLayout) findViewById(R.id.relativeSort);
    }

    public void checkRunTimePermissions(String doc_uri, String fileName) {
        this.doc_uri = doc_uri;
        this.fileName = fileName;
        DocListActivity docListActivity = (DocListActivity) mContext;
        if (ContextCompat.checkSelfPermission(DocListActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(docListActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "Please accept External Storage Permission to continue", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(docListActivity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            } else {
                ActivityCompat.requestPermissions(docListActivity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            downloadData();
        }
    }

    private void downloadData() {
        int conn = NetworkUtil.getConnectivityStatus(mContext);
        if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            stopTimer();
            networkError_dialog.showDialog(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    networkError_dialog.dismissDialog();
                    downloadData();
                }
            });
        } else {
            if (isDownloadManagerAvailable()) {
                isDownloading = true;
                pd = AppUtil.showProgressBar(mContext, getResources().getString(R.string.downloading_progress));
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(doc_uri));
                request.setDescription(getResources().getString(R.string.downloading_progress));
                request.setTitle(fileName);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    downloadReference = manager.enqueue(request);
                    startTimer();

                }
            } else {
                Snackbar snackbar = Snackbar
                        .make(rvDocList, "Doesn't contain download manager", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    }

    public static boolean isDownloadManagerAvailable() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AppUtil.dismissProgressBar(pd);
            DocsListAdapter.isClickable = true;
            String action = intent.getAction();
            if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                isDownloading = false;
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadReference);
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor cursor = null;
                try {
                    cursor = manager.query(query);
                    if (cursor.moveToFirst()) {
                        if (cursor.getCount() > 0) {
                            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                Uri contentUri = null;
                                stopTimer();
                                String downloadedFileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                                if (downloadedFileName.equalsIgnoreCase(fileName)) { // This can also be compared with reference Id
                                    String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                    if (android.os.Build.VERSION.SDK_INT >= 24) {
                                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                        m.invoke(null);
                                    }
                                    String mimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                                    openFile(uriString, mimeType);
                                }
                            } else {
                                AppUtil.dismissProgressBar(pd);
                                DocsListAdapter.isClickable = true;
                                checkDownloadStatus();
                            }
                        }
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AppUtil.dismissProgressBar(pd);
                DocsListAdapter.isClickable = true;
                if (isDownloading) {
                    int conn = NetworkUtil.getConnectivityStatus(context);
                    if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
                        stopTimer();
                        Snackbar snackbar = Snackbar
                                .make(rvDocList, "Please check your Internet connection", Snackbar.LENGTH_SHORT);

                        snackbar.show();
                    } else {
                        startTimer();
                    }
                }
            }

        }
    };

    private void openFile(String uriString, String mimeType) {
        try {
            Uri uri = Uri.parse(uriString);
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            newIntent.setDataAndType(uri, mimeType);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PackageManager packageManager = mContext.getPackageManager();
            if (newIntent.resolveActivity(packageManager) != null) {
                startActivity(newIntent);
            } else {
                Snackbar snackbar = Snackbar
                        .make(rvDocList, getResources().getString(R.string.open_file), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        } catch (ActivityNotFoundException e) {
            Snackbar snackbar = Snackbar
                    .make(rvDocList, getResources().getString(R.string.error_string), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(receiver, filter);

        if (isDownloading) {
            startTimer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();
        AppUtil.dismissProgressBar(pd);
        DocsListAdapter.isClickable = true;
        unregisterReceiver(receiver);
    }

    private void startTimer() {
        try {
            if (!isTimer) {
                isTimer = true;
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (!isDownloading) {
                            timer.cancel();
                        } else {
                            checkDownloadStatus();
                        }
                    }
                }, 0, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopTimer() {
        if (isTimer) {
            timer.cancel();
            isTimer = false;
        }
    }

    private void checkDownloadStatus() {
        try {
            DownloadManager.Query ImageDownloadQuery = new DownloadManager.Query();
            ImageDownloadQuery.setFilterById(downloadReference);
            DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = manager.query(ImageDownloadQuery);
            if (cursor.moveToFirst()) {
                DownloadStatus(cursor, downloadReference);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DownloadStatus(Cursor cursor, long DownloadId) {
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        String statusText = "";
        String reasonText = "";

        switch (status) {
            case DownloadManager.STATUS_FAILED:
                AppUtil.dismissProgressBar(pd);
                DocsListAdapter.isClickable = true;
                stopTimer();
                statusText = "STATUS_FAILED";
                switch (reason) {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                AppUtil.dismissProgressBar(pd);
                DocsListAdapter.isClickable = true;

                stopTimer();
                statusText = "STATUS_PAUSED";
                switch (reason) {
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "STATUS_PENDING";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "STATUS_RUNNING";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                stopTimer();
                statusText = "STATUS_SUCCESSFUL";
                break;
        }

        if (DownloadId == downloadReference) {
            if (status != DownloadManager.STATUS_PAUSED && status != DownloadManager.STATUS_RUNNING && status != DownloadManager.STATUS_PENDING) {
                Snackbar snackbar = Snackbar
                        .make(rvDocList, statusText, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    ActivityCompat.requestPermissions(DocListActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                        downloadData();
                    } else {
                        Toast.makeText(mContext, "Please enable permissions in settings", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
