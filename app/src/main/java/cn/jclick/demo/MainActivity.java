package cn.jclick.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.jclick.jadapter.CommonAdapter;
import cn.jclick.jadapter.CommonDataItem;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private CommonAdapter commonAdapter;
    private List<CommonDataItem> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lv_demo);
        dataSource = new ArrayList<>();
        commonAdapter = new CommonAdapter(this, dataSource, R.layout.item_first, R.layout.item_second, R.layout.item_third, R.layout.item_forth);
        listView.setAdapter(commonAdapter);

        findViewById(R.id.btn_multi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMultiItemListView();
            }
        });
        findViewById(R.id.btn_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleItemListView();
            }
        });

        commonAdapter.setViewHooker(new CommonAdapter.ViewHooker() {
            @Override
            public boolean isHookSuccess(int position, View view, Object viewData) {
                //You can custom the view by data in here.
                return false;
            }
        });
        showSingleItemListView();
    }

    private void showSingleItemListView(){
        dataSource.clear();
        for (int i = 0; i < 100; i ++){
            CommonDataItem item = new CommonDataItem(R.layout.item_first);
            item.bindView(R.id.tv_title, "item title " + i);
            item.bindView(R.id.tv_content, "item content " + i);
            dataSource.add(item);
        }
        commonAdapter.notifyDataSetChanged();
    }

    private void showMultiItemListView(){
        dataSource.clear();
        for (int i = 0; i < 100; i ++){
            int random = (int) (Math.random() * 10) % 4;
            if (random == 0){
                CommonDataItem item = new CommonDataItem(R.layout.item_first);
                item.bindView(R.id.tv_title, "item_second title " + i);
                item.bindView(R.id.tv_content, "item_second content " + i);
                dataSource.add(item);
            }else if(random == 1){
                CommonDataItem item = new CommonDataItem(R.layout.item_second);
                item.bindView(R.id.tv_title, "item_first title " + i);
                item.bindView(R.id.tv_content, "item_first content " + i);
                item.bindView(R.id.tv_tips, "item_first tips " + i);
                dataSource.add(item);
            }else if (random == 2){
                final CommonDataItem item = new CommonDataItem(R.layout.item_third);
                item.setPosition(i);
                item.bindView(R.id.tv_title, "item_third title " + i);
                item.bindView(R.id.btn_show_me, "I'm a button, click me", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Click me at position " + item.getPosition(), 0).show();
                    }
                });
                dataSource.add(item);
            }else if (random == 3){
                final CommonDataItem item = new CommonDataItem(R.layout.item_forth);
                item.setPosition(i);
                item.bindView(R.id.tv_title, "item_forth title " + i);
                item.bindView(R.id.iv_user, "http://img02.tooopen.com/images/20151229/tooopen_sy_153057917287.jpg", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Click imageView at position " + item.getPosition(), 0).show();
                    }
                });
                dataSource.add(item);
            }
        }
        commonAdapter.notifyDataSetChanged();
    }
}
