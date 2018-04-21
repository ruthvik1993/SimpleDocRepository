package apnacomplex.docrepository_anjaneya.adapters;

/**
 * Created by Ruthvik on 7/6/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import apnacomplex.docrepository_anjaneya.R;
import apnacomplex.docrepository_anjaneya.activity.DocListActivity;
import apnacomplex.docrepository_anjaneya.model.DocCategoryModel;
import apnacomplex.docrepository_anjaneya.util.AppConstants;

public class DocCategoryAdapter extends RecyclerView.Adapter<DocCategoryAdapter.ViewHolder> {
    private ArrayList<DocCategoryModel.InnerDocCategoryModel> docRepositoryArray = new ArrayList<>();
    private Context context;

    public DocCategoryAdapter(Context context, ArrayList<DocCategoryModel.InnerDocCategoryModel> repositoryArray) {
        this.docRepositoryArray = repositoryArray;
        this.context = context;
    }

    @Override
    public DocCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item_doc_category, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DocCategoryAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.txtCatName.setText(docRepositoryArray.get(i).getCat_name());
        viewHolder.txtNumDocs.setText("(" + docRepositoryArray.get(i).getNum_docs() + ")");

        String[] separated = docRepositoryArray.get(i).getCat_icon().split("\\.");
        String uri = "@drawable/" + separated[0];
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Drawable res = ContextCompat.getDrawable(context, imageResource);
        viewHolder.imgCatRep.setImageDrawable(res);

        separated = docRepositoryArray.get(i).getCat_background_img().split("\\.");
        uri = "@drawable/" + separated[0];
        imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            viewHolder.linearCard.setBackgroundDrawable(ContextCompat.getDrawable(context, imageResource));
        } else {
            viewHolder.linearCard.setBackground(ContextCompat.getDrawable(context, imageResource));
        }


        viewHolder.linearCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DocListActivity.class);
                intent.putExtra(AppConstants.CATEGORY_ID, docRepositoryArray.get(i).getCat_id());
                intent.putExtra(AppConstants.CATEGORY_NAME, docRepositoryArray.get(i).getCat_name());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return docRepositoryArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtCatName;
        private TextView txtNumDocs;
        private ImageView imgCatRep;
        private LinearLayout linearCard;

        public ViewHolder(View view) {
            super(view);
            linearCard = (LinearLayout) view.findViewById(R.id.linearCard);
            txtCatName = (TextView) view.findViewById(R.id.txtCatName);
            txtNumDocs = (TextView) view.findViewById(R.id.txtNumDocs);
            imgCatRep = (ImageView) view.findViewById(R.id.imgCatRep);

        }
    }

}
