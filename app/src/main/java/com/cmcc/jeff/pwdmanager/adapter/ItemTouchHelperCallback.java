package com.cmcc.jeff.pwdmanager.adapter;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by jeff on 2017/3/28.
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private IItemTouchHelperAdapter itemTouchHelperAdapter;
    private float ALPHA_FULL = 1.0f;

    public ItemTouchHelperCallback(IItemTouchHelperAdapter itemTouchHelperAdapter) {
        this.itemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    /**
     * RecyclerView item支持长按进入拖动操作
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * RecyclerView item任意位置触发启用滑动操作
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    /**
     * 指定可以支持的拖放和滑动的方向，上下为拖动（drag），左右为滑动（swipe）
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager || recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            //不需要滑动
            final int swapFlags = 0;
            return  makeMovementFlags(dragFlags, swapFlags);

        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swapFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swapFlags);
        }
    }

    /**
     * 滑动操作
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if(viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        //notify adapter of the move
        itemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * 删掉操作
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        itemTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //自定义滑动动画
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof IItemTouchHelperViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                IItemTouchHelperViewHolder itemViewHolder = (IItemTouchHelperViewHolder) viewHolder;
                //选中状态回调
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(ALPHA_FULL);
        if (viewHolder instanceof IItemTouchHelperViewHolder) {
            // Tell the view holder it's time to restore the idle state
            IItemTouchHelperViewHolder itemViewHolder = (IItemTouchHelperViewHolder) viewHolder;
            //未选中状态回调
            itemViewHolder.onItemClear();
        }
    }

}
