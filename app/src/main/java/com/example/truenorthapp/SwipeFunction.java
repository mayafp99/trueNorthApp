package com.example.truenorthapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truenorthapp.Adapter.TaskAdapter;

// functions that work to allow the swipe features.
public class SwipeFunction extends ItemTouchHelper.SimpleCallback
{
    private TaskAdapter a;
    public SwipeFunction(TaskAdapter a)
    {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.a = a;
    }

    // a boolean to record and respond to whenever a task is actually being shifted.
    @Override
    public boolean onMove(RecyclerView rv, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        return false;
    }

    // records when the task is swiped left, and responds to this by popping up a deletion confirmation message.
    // there doesn't seem to be a way to customize this builder popup either...
    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int side)
    {
        final int direction = viewHolder.getAbsoluteAdapterPosition();
        if(side == ItemTouchHelper.LEFT)
        {
            AlertDialog.Builder b = new AlertDialog.Builder(a.getContext());
            b.setTitle("Delete Task");
            b.setMessage("Have you lost your way?");
            b.setPositiveButton("Yes, delete this task...",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            a.deleteItem(direction);
                        }
                    });
            b.setNegativeButton("No, return me.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    a.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                }
            });
            AlertDialog d = b.create();
            d.show();
        }
        // rules out all other options and uses else to decide if the task is swiped right.
        // if it is, the task is edited from the adapter.
        else
        {
            a.editTask(direction);
        }
    }

    // visual elements of swiping left or right.
    @Override
    public void onChildDraw(Canvas c, RecyclerView rv, RecyclerView.ViewHolder vh, float dX, float dY, int actState, boolean active)
    {
        super.onChildDraw(c, rv, vh, dX, dY, actState, active);
        Drawable icon;
        ColorDrawable background;

        View itemView = vh.itemView;
        int backcornerOffset = 15;

        if(dX > 0)
        {
           icon = ContextCompat.getDrawable(a.getContext(), R.drawable.edit);
           background = new ColorDrawable(ContextCompat.getColor(a.getContext(), R.color.fab));
        }
        else
        {
            icon = ContextCompat.getDrawable(a.getContext(), R.drawable.delete);
            background = new ColorDrawable(ContextCompat.getColor(a.getContext(), R.color.delete));
        }

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBot = iconTop + icon.getIntrinsicHeight();
        // when swiping right
        if(dX > 0)
        {
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBot);
            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int)dX) + backcornerOffset, itemView.getBottom());
        }
        // swiping right
        else if(dX < 0)
        {
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBot);
            background.setBounds(itemView.getRight() + ((int)dX) - backcornerOffset, itemView.getTop(),
                    itemView.getRight(), itemView.getBottom());
        }
        else
        {
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
        icon.draw(c);
    }
}
