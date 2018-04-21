package apnacomplex.docrepository_anjaneya.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.ArrayList;

import apnacomplex.docrepository_anjaneya.R;
import apnacomplex.docrepository_anjaneya.adapters.DocCategoryAdapter;
import apnacomplex.docrepository_anjaneya.adapters.SpacesItemDecoration;
import apnacomplex.docrepository_anjaneya.dialog.NetworkError_Dialog;
import apnacomplex.docrepository_anjaneya.model.DocCategoryModel;
import apnacomplex.docrepository_anjaneya.network.ApiConstants;
import apnacomplex.docrepository_anjaneya.network.DocRepositoryRequest;
import apnacomplex.docrepository_anjaneya.network.DocRepositoryResponse;
import apnacomplex.docrepository_anjaneya.network.NetworkManager;
import apnacomplex.docrepository_anjaneya.util.AppConstants;
import apnacomplex.docrepository_anjaneya.util.AppUtil;

import static apnacomplex.docrepository_anjaneya.util.AppUtil.checkNetworkShowDialog;

public class DocumentCategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar myToolbar;
    private Context mContext;
    private FloatingActionButton fab;
    private NetworkError_Dialog networkError_dialog;
    private ArrayList<DocCategoryModel.InnerDocCategoryModel> docCatArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_category);
        init();
        onClicks();
        setActionBar();
        getDocCategories();
        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (networkError_dialog != null) {
            checkNetworkShowDialog(mContext);
        } //check network connection and show dialog
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(recyclerView, getResources().getString(R.string.fab_message), Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }

    private void setActionBar() {
        String title = "Repository";
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_ic);
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'> " + title + "</font>"));
        }
    }

    private void getDocCategories() {
        final ProgressDialog pd = AppUtil.showProgressBar(mContext, getResources().getString(R.string.progress_message));
        DocRepositoryRequest docRepositoryRequest = new DocRepositoryRequest(mContext, DocRepositoryRequest.Method.GET,
                ApiConstants.ApiName.DOCUMENT_CATEGORIES, new Response.Listener<DocRepositoryResponse>() {
            @Override
            public void onResponse(DocRepositoryResponse response) {
                AppUtil.dismissProgressBar(pd);
                if (response.isSuccess()) {
                    Gson gson = new Gson();
                    DocCategoryModel docCategoryModel = gson.fromJson(response.getResponseBody().toString(), DocCategoryModel.class);
                    docCatArrayList = docCategoryModel.getDocumentCategories();
                    setAdapter();
                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {
                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                    networkError_dialog.showDialog(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            networkError_dialog.dismissDialog();
                            getDocCategories();
                        }
                    });
                } else {
                    Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);

        NetworkManager.getInstance(mContext).sendRequest(docRepositoryRequest);

    }

    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        DocCategoryAdapter docCatAdapter = new DocCategoryAdapter(mContext, docCatArrayList);
        recyclerView.addItemDecoration(new SpacesItemDecoration(5, 1));
        recyclerView.setAdapter(docCatAdapter);
    }

    private void init() {
        mContext = DocumentCategoryActivity.this;
        myToolbar = (Toolbar) findViewById(R.id.cat_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.rvDocCategory);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        networkError_dialog = new NetworkError_Dialog(mContext);
    }


}
