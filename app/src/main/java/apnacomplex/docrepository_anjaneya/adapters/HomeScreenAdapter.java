package apnacomplex.docrepository_anjaneya.adapters;

/**
 * Created by Ruthvik on 7/6/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import apnacomplex.docrepository_anjaneya.R;
import apnacomplex.docrepository_anjaneya.activity.DocumentCategoryActivity;
import apnacomplex.docrepository_anjaneya.model.HomeScreenModel;

public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.ViewHolder> {
    private ArrayList<HomeScreenModel> repositoryArray = new ArrayList<>();
    private Context context;

    public HomeScreenAdapter(Context context, ArrayList<HomeScreenModel> repositoryArray) {
        this.repositoryArray = repositoryArray;
        this.context = context;
    }

    @Override
    public HomeScreenAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item_home, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeScreenAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.txtRep.setText(repositoryArray.get(i).getName());
        viewHolder.imgRep.setImageResource(repositoryArray.get(i).getImgResId());
        viewHolder.linearhomeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    Intent intent = new Intent(context, DocumentCategoryActivity.class);
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return repositoryArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtRep;
        private ImageView imgRep;
        private CardView cardView;
        private LinearLayout linearhomeItem;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            txtRep = (TextView) view.findViewById(R.id.txtRep);
            imgRep = (ImageView) view.findViewById(R.id.imgRep);
            linearhomeItem = (LinearLayout) view.findViewById(R.id.linearhomeItem);

        }
    }

}
