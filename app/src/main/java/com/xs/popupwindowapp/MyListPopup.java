package com.xs.popupwindowapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xs.popupwindowlib.base.anim.AnimPopupWindow;
import com.xs.popupwindowlib.util.SimpleAnimUtil;

import java.util.List;

/**
 * @Description
 * @Author xs.lin
 * @Date 2017/3/23 14:53
 */

public class MyListPopup extends AnimPopupWindow {

    private ListView listView;
    private PopupListAdapter adapter;

    public MyListPopup(Activity activity, List<String> stringList) {
        this(activity, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,stringList);
    }

    private MyListPopup(Activity activity, int width, int height, List<String> stringList) {
        super(activity, width, height);

        listView = (ListView) findViewById(R.id.popup_list);
        adapter = new PopupListAdapter(activity,stringList);
        listView.setAdapter(adapter);

    }

    @Override
    protected View setPopupContentView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.list_layout, null);
    }

    @Override
    protected boolean setOutsideTouchable() {
        return true;
    }

    @Override
    protected Animation setEnterAnimation() {
        return SimpleAnimUtil.getScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
    }

    @Override
    protected Animation setExitAnimation() {
        return SimpleAnimUtil.getScaleAnimation(1.0f, 1.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
    }

    public void update(List<String> updateList) {
        adapter.update(updateList);
    }

    private static class PopupListAdapter extends BaseAdapter {

        private Context context;
        private List<String> list;
        PopupListAdapter(Activity activity, List<String> list) {
            this.list = list;
            this.context = activity;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_popuplist,parent,false);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(list.get(position));
            return convertView;
        }

        public void update(List<String> updateList) {
            list = updateList;
            notifyDataSetChanged();
        }
        class ViewHolder {
            TextView textView;
        }
    }
}
