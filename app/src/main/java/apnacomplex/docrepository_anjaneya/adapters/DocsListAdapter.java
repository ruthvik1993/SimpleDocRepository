package apnacomplex.docrepository_anjaneya.adapters;

/**
 * Created by Ruthvik on 7/6/2017.
 */

import android.Manifest;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import apnacomplex.docrepository_anjaneya.R;
import apnacomplex.docrepository_anjaneya.activity.DocListActivity;
import apnacomplex.docrepository_anjaneya.model.DocsListModel;

public class DocsListAdapter extends RecyclerView.Adapter<DocsListAdapter.ViewHolder> {


    private ArrayList<DocsListModel.DocumentsModel> documentsList = new ArrayList<>();
    private Context context;
    public static boolean isClickable = true;

    public DocsListAdapter(Context context, ArrayList<DocsListModel.DocumentsModel> documentsList) {
        this.documentsList = documentsList;
        this.context = context;
    }

    @Override
    public DocsListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item_docs_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DocsListAdapter.ViewHolder viewHolder, final int i) {
        if (documentsList.get(i) != null) {
            viewHolder.txtTitle.setText(documentsList.get(i).getDoc_name());
            viewHolder.txtFileSize.setText("File Size " + documentsList.get(i).getDoc_size());

            if (documentsList.get(i).getDoc_type() != null && !documentsList.get(i).getDoc_type().contains("pdf")) {
                Picasso.with(context)
                        .load(documentsList.get(i).getDoc_url())
                        .into(viewHolder.imgThumbnail);
            } else {
                viewHolder.imgThumbnail.setImageResource(R.drawable.dummy4_bg);
            }

            viewHolder.imgMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayPopWindow(v, i, documentsList.get(i).getDoc_name());
                }
            });
            viewHolder.rvImgText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickable) {
                        isClickable = false;
                        ((DocListActivity) context).checkRunTimePermissions(documentsList.get(i).doc_url, documentsList.get(i).getDoc_name());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return documentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgThumbnail;
        private TextView txtTitle;
        private TextView txtFileSize;
        private ImageView imgMenu;
        private RelativeLayout rvImgText;

        public ViewHolder(View view) {
            super(view);
            imgThumbnail = (ImageView) view.findViewById(R.id.imgThumbnail);
            txtFileSize = (TextView) view.findViewById(R.id.txtFileSize);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            imgMenu = (ImageView) view.findViewById(R.id.imgMenu);
            rvImgText = (RelativeLayout) view.findViewById(R.id.rvImgText);
        }
    }

    private void displayPopWindow(View v, final int position, final String fileName) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(R.menu.overflow_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.overflow_view:
                        if (context instanceof DocListActivity) {
                            ((DocListActivity) context).checkRunTimePermissions(documentsList.get(position).doc_url, fileName);
                        }
                        return true;
                }
                return true;
            }

        });
        popup.show();
    }


}
