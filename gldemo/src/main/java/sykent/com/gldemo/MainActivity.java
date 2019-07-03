package sykent.com.gldemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sykent.framework.activity.BaseActivity;
import com.sykent.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import sykent.com.gldemo.activity.BlendActivity;
import sykent.com.gldemo.activity.PlayVideoActivity;
import sykent.com.gldemo.main.MainItemData;
import sykent.com.gldemo.main.MainPageAdapter;
import sykent.com.gldemo.main.SpaceItemDecoration;

public class MainActivity extends BaseActivity {

    @BindView(R.id.ll_root)
    LinearLayout mRoot;
    @BindView(R.id.main_page_rv)
    RecyclerView mRecyclerView;

    private MainPageAdapter mMainPageAdapter;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListener();
    }


    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState, Intent intent) {
        super.initData(savedInstanceState, intent);

        List<MainItemData> datas = new ArrayList<>();
        datas.add(new MainItemData("视频播放"));
        datas.add(new MainItemData("GL混合"));
        datas.add(new MainItemData("探索Demo"));
        datas.add(new MainItemData("探索Demo"));
        datas.add(new MainItemData("探索Demo"));
        datas.add(new MainItemData("探索Demo"));

        int spanCount = 3;
        mGridLayoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        int itemSpace = Utils.getRealPixel(10);
        mRecyclerView.setPadding(itemSpace, itemSpace, 0, 0);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(itemSpace));

        mMainPageAdapter = new MainPageAdapter(this, itemSpace, spanCount);
        mMainPageAdapter.setItemDatas(datas);
        mRecyclerView.setAdapter(mMainPageAdapter);
        mMainPageAdapter.setItemClickListener(new MainPageAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(int position) {
                jumpPage(position);
            }
        });
    }

    private void jumpPage(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(this, PlayVideoActivity.class);
                this.startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, BlendActivity.class);
                this.startActivity(intent);
                break;
            default:
        }
    }

    @Override
    public int provideContentViewLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    public int provideTitleViewLayoutResID() {
        return R.layout.main_titile;
    }

    private void initListener() {

    }
}
