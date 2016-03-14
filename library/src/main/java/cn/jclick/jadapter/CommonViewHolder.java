package cn.jclick.jadapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class CommonViewHolder {
    private static final String TAG = CommonViewHolder.class.getName();
    public final WeakReference<View> itemViewWeakReference;
    private final SparseArray<View> mReuseViewArray = new SparseArray<>();

    public CommonViewHolder(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }
        this.itemViewWeakReference = new WeakReference<View>(itemView);
    }

    public View findViewById(int id){
        View view = mReuseViewArray.get(id);
        if (itemViewWeakReference.get() == null){
            throw new IllegalStateException("view is recycled");
        }
        if (view == null){
            view = itemViewWeakReference.get().findViewById(id);
            if (view != null){
                mReuseViewArray.put(id, view);
            }
        }
        if (view == null){
            throw new IllegalStateException(String.format("can not findViewById %s  %d, so return null", getIdNameById(id), id));
        }
        return view;
    }

    public ImageView findImageViewById(int id){
        View view = findViewById(id);
        if (!(view instanceof ImageView)){
            throw new IllegalStateException(String.format("can not convert %s to ImageView", view.getClass().getCanonicalName()));
        }
        return (ImageView) view;
    }

    public TextView findTextViewById(int id){
        View view = findViewById(id);
        if (!(view instanceof TextView)){
            throw new IllegalStateException(String.format("can not convert %s to TextView, the view id is %s ", view.getClass().getCanonicalName(), getIdNameById(id)));
        }
        return (TextView) view;
    }

    public Button findButtonById(int id){
        View view = findViewById(id);
        if (!(view instanceof Button)){
            throw new IllegalStateException(String.format("can not convert %s to Button, the view id is %s ", view.getClass().getCanonicalName(), getIdNameById(id)));
        }
        return (Button) view;
    }

    public ImageButton findImageButtonById(int id){
        View view = findViewById(id);
        if (!(view instanceof ImageButton)){
            throw new IllegalStateException(String.format("can not convert %s to ImageButton, the view id is %s ", view.getClass().getCanonicalName(), getIdNameById(id)));
        }
        return (ImageButton) view;
    }

    private String getIdNameById(int id){
        if (itemViewWeakReference.get() == null){
            return null;
        }
        return itemViewWeakReference.get().getContext().getResources().getResourceEntryName(id);
    }
}
