package com.yu.common.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.yu.common.framework.ItemClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context mContext;
  private List<T> mList = new ArrayList<>();

  private ItemClickListener<T> itemClickListener;

  public ItemClickListener<T> getItemClickListener() {
    return itemClickListener;
  }

  public void setItemClickListener(ItemClickListener<T> itemClickListener) {
    this.itemClickListener = itemClickListener;
    notifyDataSetChanged();
  }

  public BaseRecyclerAdapter(Context context) {
    this.mContext = context;
  }

  public Context getContext() {
    return mContext;
  }

  public List<T> getList() {
    return mList;
  }

  public int findPosition(T t) {
    int index = this.getItemCount();
    int position = -1;

    String targetKey = "" + getItemKey(t);

    while (index-- > 0) {
      T it = getItem(index);
      if (it != null && targetKey.equals(getItemKey(it))) {
        position = index;
        break;
      }
    }

    return position;
  }

  public void setCollection(Collection<T> collection) {
    this.mList.clear();
    addCollection(collection);
  }

  public void addCollection(Collection<T> collection) {
    if (collection != null) {
      this.mList.addAll(collection);
    }
    notifyDataSetChanged();
  }

  public void addCollection(int pos, Collection<T> collection) {
    if (collection != null) {
      if (pos >= mList.size()) {
        this.mList.addAll(collection);
      } else {
        this.mList.addAll(pos, collection);
      }
    }
    notifyDataSetChanged();
  }

  public void add(T t) {
    if (t != null && !mList.contains(t)) {
      this.mList.add(t);
      notifyDataSetChanged();
    }
  }

  public void remove(int position) {
    if (position >= 0 && position < mList.size()) {
      this.mList.remove(position);
      notifyDataSetChanged();
    }
  }

  public T getItem(int pos) {
    if (pos >= 0 && pos < mList.size()) {
      return mList.get(pos);
    }
    return null;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = newView(mContext, parent, viewType);
    return new RecyclerView.ViewHolder(view) {
    };
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView")
  final int position) {

    if (itemClickListener != null) {
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          handleItemClick(position, getItem(position));
          if (itemClickListener != null) {
            itemClickListener.onItemClick(position, getItem(position));
          }
        }
      });
    } else {
      boolean clickable = holder.itemView.isClickable();
      holder.itemView.setOnClickListener(null);
      holder.itemView.setClickable(clickable);
    }

    bindView(holder, position, position < mList.size() ? mList.get(position) : null);
  }

  @Override public int getItemCount() {
    return this.mList == null ? 0 : this.mList.size();
  }

  /**
   * itemClick事件
   */
  protected void handleItemClick(int pos, T t) {
    //before itemClick
  }

  protected String getItemKey(T t) {
    return String.valueOf(t);
  }

  protected abstract View newView(Context context, ViewGroup viewGroup, int viewType);

  protected abstract void bindView(RecyclerView.ViewHolder holder, int pos, T t);

  /**
   * 清理数据
   */
  public void clear() {
    this.mList.clear();
    this.itemClickListener = null;
    this.mContext = null;
  }
}
