package cn.jclick.jadapter;

import android.util.SparseArray;

/**
 * Created by apple on 16/3/11.
 */
public class CommonDataItem {

    public static final int VIEW_TYPE_NONE = -1;
    private SparseArray<Object[]> dataArray;
    private final int layoutId;
    private int position;
    private Object tag;
    private int viewType = VIEW_TYPE_NONE;
    public CommonDataItem(int layoutId){
        this.layoutId = layoutId;
        dataArray = new SparseArray<>();
    }

    public void bindView(int id, Object ...objectArray){
        dataArray.put(id, objectArray);
    }

    public void addBind(int id, Object ...objectArray){
        Object[] oldArr = dataArray.get(id);
        if (oldArr == null || oldArr.length == 0){
            dataArray.put(id, objectArray);
            return;
        }
        if(objectArray != null && objectArray.length > 0){
            Object[] newArray = new Object[objectArray.length + oldArr.length];
            System.arraycopy(oldArr, 0, newArray, 0, oldArr.length);
            System.arraycopy(newArray, 0, newArray, oldArr.length, newArray.length);
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public SparseArray<Object[]> getDataArray() {
        return dataArray;
    }

    public int getLayoutId() {
        return layoutId;
    }
}
