package sykent.com.gldemo.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sykent.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sykent.com.gldemo.R;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/01
 */
public class MainPageAdapter extends RecyclerView.Adapter<MainPageAdapter.ViewHolder> {
    private Context mContext;
    private int mItemSpace;
    private int mSpaceCount;
    private List<MainItemData> mItemDatas;

    public MainPageAdapter(Context context, int itemSpace, int spaceCount) {
        mContext = context;
        mItemSpace = itemSpace;
        mSpaceCount = spaceCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.mRoot.getLayoutParams();
        int size = (Utils.getScreenWidth() - mItemSpace * (mSpaceCount + 1)) / mSpaceCount;
        layoutParams.width = size;
        layoutParams.height = size;
        holder.mRoot.setLayoutParams(layoutParams);
        holder.mRoot.setTag(position);

        MainItemData data = mItemDatas.get(position);
        holder.mShowTips.setText(data.getTips());
    }

    @Override
    public int getItemCount() {
        return mItemDatas != null ? mItemDatas.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.main_view_holder_root)
        View mRoot;
        @BindView(R.id.main_view_holder_tv)
        TextView mShowTips;

        @OnClick({R.id.main_view_holder_root})
        public void onClick(View view) {
            if (view.getTag() instanceof Integer) {
                int position = (int) view.getTag();
                if (mItemClickListener != null) {
                    mItemClickListener.onClickItem(position);
                }
            }
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setItemDatas(List<MainItemData> itemDatas) {
        mItemDatas = itemDatas;
    }

    private OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onClickItem(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
