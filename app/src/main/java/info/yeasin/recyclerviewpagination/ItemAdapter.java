package info.yeasin.recyclerviewpagination;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import info.yeasin.recyclerviewpagination.model.Hit;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Hit> Items = new ArrayList<>();
    OnItemClickListener mItemClickListener;
    Context mContext;

    public ItemAdapter(Context context) {
        Items = new ArrayList<>();
        mContext = context;
    }

    public ItemAdapter(List<Hit> items) {
        this.Items = items;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtFirstName, txtLastName;
        ImageView imgUserAvatar;

        public ItemViewHolder(View view) {
            super(view);
            imgUserAvatar = (ImageView)view.findViewById(R.id.img_user_avatar);
            txtFirstName = (TextView) view.findViewById(R.id.txt_first_name);
            txtLastName = (TextView) view.findViewById(R.id.txt_last_name);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recyclerview, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Hit model = Items.get(position);

        Picasso.get().load(model.getLargeImageURL()).into(holder.imgUserAvatar);
        holder.txtFirstName.setText("First Name: "+ model.getFavorites());
        holder.txtLastName.setText("Last Name: "+ model.getTags());
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClicklListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setItems(List<Hit> items){
        Items = items;
        notifyDataSetChanged();
    }

    public void addItems(List<Hit> items){
        Items.addAll(items);
        notifyDataSetChanged();;
    }
}
