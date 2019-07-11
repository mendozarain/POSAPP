package pos.app.apollo.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import pos.app.apollo.Models.ProductModel;
import pos.app.sample.R;

public class ProductAdapters extends RecyclerView.Adapter<ProductAdapters.ViewHolder> {
    private Context baseContext;
    private Context applicationContext;
    private List<ProductModel> productModelList;

    public ProductAdapters(Context mBaseContext, Context mApplicationContext, List<ProductModel> productModelList) {
        baseContext = mBaseContext;
        applicationContext = mApplicationContext;
        this.productModelList = productModelList;


    }


    @Override
    public ProductAdapters.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(baseContext).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel productModel = productModelList.get(position);
        holder.tvName.setText(String.format("" + productModel.getName()));
        holder.tvPrice.setText(String.format("" + productModel.getPrice()));

        Glide.with(applicationContext).load("https://via.placeholder.com/160x190").into(holder.imgItem);
    }


    @Override
    public int getItemCount() {
        return 20;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgItem;
        public TextView tvName;
        public TextView tvPrice;


        public ViewHolder(final View itemView) {
            super(itemView);
            imgItem = (ImageView) itemView.findViewById(R.id.item_img);
            tvName = (TextView) itemView.findViewById(R.id.item_name);
            tvPrice = (TextView) itemView.findViewById(R.id.item_price);
        }
    }
}