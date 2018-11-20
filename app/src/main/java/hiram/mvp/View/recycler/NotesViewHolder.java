package hiram.mvp.View.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hiram.mvp.R;

public class NotesViewHolder extends RecyclerView.ViewHolder {

    public CardView container;
    public TextView text, date;
    public ImageButton btnDelete;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        setupViews(itemView);
    }

    private void setupViews(View view){
        container = view.findViewById(R.id.holder_container);
        text = view.findViewById(R.id.tv_Note);
        date = view.findViewById(R.id.tv_Date);
        btnDelete = view.findViewById(R.id.imgbtn_delete);

    }
}
