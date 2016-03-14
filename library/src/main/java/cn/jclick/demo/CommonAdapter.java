package cn.jclick.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

/**
 * Created by apple on 16/3/11.
 */
public class CommonAdapter extends BaseAdapter{

    private int[] mLayoutArray;
    private List<CommonDataItem> mDataSource;
    private ViewHooker mViewHooker;
    private LayoutInflater mInflater;
    private Context mContext;

    public CommonAdapter(Context context, List<CommonDataItem> mDataSource, int... layoutIds){
        if (layoutIds == null || layoutIds.length == 0){
            throw new IllegalArgumentException("layoutIds can not empty");
        }
        this.mLayoutArray = layoutIds;
        this.mDataSource = mDataSource;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return mDataSource == null ? 0 : mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return this.mLayoutArray.length;
    }

    @Override
    public int getItemViewType(int position) {
        if (mLayoutArray.length == 0){
            return 0;
        }
        CommonDataItem dataItem = mDataSource.get(position);
        if (dataItem.getViewType() == CommonDataItem.VIEW_TYPE_NONE){
            for (int i = 0; i < mLayoutArray.length; i ++){
                if (dataItem.getLayoutId() == mLayoutArray[i]){
                    dataItem.setViewType(i);
                    return dataItem.getViewType();
                }
            }
        }else{
            return dataItem.getViewType();
        }
        throw new RuntimeException("can not getItemViewType. check your layoutIds params");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonDataItem dataItem = mDataSource.get(position);
        if (convertView == null){
            convertView = mInflater.inflate(dataItem.getLayoutId(), parent, false);
        }
        CommonViewHolder viewHolder = (CommonViewHolder) convertView.getTag();
        if (viewHolder == null){
            viewHolder = new CommonViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        SparseArray<Object[]> dataArray = dataItem.getDataArray();
        for (int i = 0; i < dataArray.size(); i ++){
            int viewId = dataArray.keyAt(i);
            Object[] data = dataArray.get(viewId);
            View view = viewHolder.findViewById(viewId);
            bindView(position, view, data);
        }
        return convertView;
    }

    private void bindView(int position, View view, Object[] dataArray) {
        if (dataArray == null || dataArray.length == 0){
            if (mViewHooker == null || !mViewHooker.isHookSuccess(position, view, null)) {
                clearViewBind(view);
            }
            return;
        }
        for (Object object : dataArray){
            if (mViewHooker != null && mViewHooker.isHookSuccess(position, view, object)){
                continue;
            }
            if (object instanceof View.OnTouchListener) {
                view.setOnTouchListener((View.OnTouchListener) object);
                continue;
            }
            if (object instanceof View.OnClickListener) {
                view.setOnClickListener((View.OnClickListener) object);
                continue;
            }
            if (object instanceof View.OnLongClickListener) {
                view.setOnLongClickListener((View.OnLongClickListener) object);
                continue;
            }
            if (view instanceof Checkable) {
                if (object instanceof Boolean) {
                    ((Checkable) view).setChecked((Boolean) object);
                } else {
                    throw new IllegalStateException(object.getClass().getName() +
                            " should be bind to a Boolean, not a " + object.getClass());
                }
            }else if (object instanceof Boolean) {
                Boolean isVisible = (Boolean) object;
                if (isVisible) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            } else if (view instanceof TextView && object instanceof CharSequence) {
                ((TextView)view).setText((CharSequence) object);
            } else if (view instanceof ImageView) {
                bindImageView((ImageView) view, object);
            } else {
                throw new IllegalStateException(view.getClass().getName() + " can not bind with value(" + object + ") by " + CommonAdapter.this.getClass().getName());
            }
        }
    }

    private void bindImageView(ImageView iv, Object data) {
        if (data instanceof Integer) {
            int d = (Integer) data;
            iv.setImageResource(d);
        } else if (data instanceof Bitmap) {
            iv.setImageBitmap((Bitmap) data);
        } else if (data instanceof String) {
            String str = (String) data;
            if (str.startsWith("http://") || str.startsWith("https://")){
                Glide.with(mContext).load(str).into(iv);
            }else{
                String path = Uri.fromFile(new File((String) data)).toString();
                Glide.with(mContext).load(new File(path)).into(iv);
            }
        } else if (data instanceof Uri) {
            Uri uri = (Uri) data;

            Glide.with(mContext).load(uri).into(iv);
        } else {
            return;
        }
    }

    private void clearViewBind(View view) {
        if (view instanceof TextView){
            ((TextView)view).setText(null);
        }else if (view instanceof ImageView) {
            ImageView iv = (ImageView) view;
            iv.setImageBitmap(null);
        }
    }

    public void setViewHooker(ViewHooker hooker){
        this.mViewHooker = hooker;
    }

    public interface ViewHooker{

        boolean isHookSuccess(int position, View view, Object viewData);
    }
}
