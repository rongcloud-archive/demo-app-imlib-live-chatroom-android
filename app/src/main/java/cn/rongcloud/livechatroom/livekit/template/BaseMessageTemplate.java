package cn.rongcloud.livechatroom.livekit.template;

import android.view.View;
import android.view.ViewGroup;


public interface BaseMessageTemplate<MessageContent> {
    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     * @param parent The parent that this view will eventually be attached to
     * @param data The data that will bind in convertView
     * @return A View corresponding to the data at the specified position.
     */
    public View getView(View convertView, int position, ViewGroup parent, UIMessage data);

    /**
     * View的点击事件。
     *
     * @param view     所点击的View。
     * @param position 点击的位置。
     * @param data     点击的消息。
     */
    public void onItemClick(View view, int position, UIMessage data);

    /**
     * View的长按事件。
     *
     * @param view     所长按的View。
     * @param position 长按的位置。
     * @param data     长按的消息。
     */
    public void onItemLongClick(View view, int position, UIMessage data);

    /**
     * 销毁消息模板。
     *
     * @param group    该模板所属的父 view。
     * @param template 模板自身。
     */
    public void destroyItem(ViewGroup group, Object template);

}
