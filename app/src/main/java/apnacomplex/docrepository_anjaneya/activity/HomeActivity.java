package apnacomplex.docrepository_anjaneya.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

import apnacomplex.docrepository_anjaneya.R;
import apnacomplex.docrepository_anjaneya.adapters.HomeScreenAdapter;
import apnacomplex.docrepository_anjaneya.adapters.SpacesItemDecoration;
import apnacomplex.docrepository_anjaneya.dialog.NetworkError_Dialog;
import apnacomplex.docrepository_anjaneya.model.HomeScreenModel;
import apnacomplex.docrepository_anjaneya.util.AppUtil;
import apnacomplex.docrepository_anjaneya.views.GridSpacingItemDecoration;

import static apnacomplex.docrepository_anjaneya.util.AppUtil.checkNetworkShowDialog;

public class HomeActivity extends AppCompatActivity {
    private ArrayList<HomeScreenModel> homeScreenArray = new ArrayList<>();
    private RecyclerView recyclerView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = HomeActivity.this;
        initializeViews();
        showMyActionBar();
        addData();
        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showMyActionBar() {
        String title = getResources().getString(R.string.app_name);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) { //Setting ActionBar elements
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_ic);
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'> " + title + "</font>"));
        }
    }

    //------------------- Setting Recycler GridView Adapter   -----------------------//
    private void setAdapter() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        HomeScreenAdapter repAdapter = new HomeScreenAdapter(mContext, homeScreenArray);
        int spanCount = 2; // 3 columns
        int spacing = getScreenWidth();
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacing, spanCount));
        recyclerView.setAdapter(repAdapter);
    }

    private int getScreenWidth() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        //int screenHeight = displayMetrics.heightPixels;
        int spacing = (screenWidth * 25) / 1080;
        return spacing;
    }

    private void addData() {
        HomeScreenModel repBean = new HomeScreenModel();
        repBean.setName("REPOSITORY");
        repBean.setImgResId(R.drawable.repository_bg);
        homeScreenArray.add(repBean);

        repBean = new HomeScreenModel();
        repBean.setName("LOREM IPSUM");
        repBean.setImgResId(R.drawable.dummy1_bg);
        homeScreenArray.add(repBean);

        repBean = new HomeScreenModel();
        repBean.setName("LOREM IPSUM");
        repBean.setImgResId(R.drawable.dummy2_bg);
        homeScreenArray.add(repBean);

        repBean = new HomeScreenModel();
        repBean.setName("LOREM IPSUM");
        repBean.setImgResId(R.drawable.dummy_3bg);
        homeScreenArray.add(repBean);

        repBean = new HomeScreenModel();
        repBean.setName("LOREM IPSUM");
        repBean.setImgResId(R.drawable.dummy4_bg);
        homeScreenArray.add(repBean);


    }

    private void initializeViews() {

        recyclerView = (RecyclerView) findViewById(R.id.rvHomeScreen);

    }
}
