package hiram.mvp.Presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hiram.mvp.MainMVP;
import hiram.mvp.R;
import hiram.mvp.View.recycler.NotesViewHolder;
import hiram.mvp.models.Note;

public class MainPresenter implements MainMVP.ProvidedPresenter, MainMVP.RequiredPresenter {

    //Referencia de la vista, se usa weakreference porque la actividad puede ser destruida en cualquier
    //momento y no queremos crear fuga de memoria
    private WeakReference<MainMVP.RequiredView> view;
    //Model REference
    private MainMVP.ProvidedModel model;

    public MainPresenter(MainMVP.RequiredView mview) {
        view = new WeakReference<>(mview);
    }



    private MainMVP.RequiredView getView() throws  NullPointerException{
        if ( view != null)
            return view.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    @Override
    public void onDestroy(boolean insChangingConfiguration) {
        view =  null;
        model.onDestroy(insChangingConfiguration);

        if(!insChangingConfiguration){
            model = null;
        }
    }

    @Override
    public void setView(MainMVP.RequiredView view) {
        this.view = new WeakReference<>(view);
    }

    public void setModel(MainMVP.ProvidedModel providedModel){
        model= providedModel;
        loadData();
    }

    private void loadData() {
        try {
            getView().showProgress();

            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    // Load data from Model
                    return model.loadData();
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    try {
                        getView().hideProgress();
                        if (!result) // Loading error
                            getView().showToast(makeToast("Error loading data."));
                        else // success
                            getView().notifyDataSetChanged();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Toast makeToast(String msg){
        return Toast.makeText(getView().getAppContext(), msg, Toast.LENGTH_LONG);
    }

    @Override
    public void clickDeleteNote(Note note, int adapterPos, int layoutPos) {
        openDeleteAlert(note, adapterPos, layoutPos);
    }

    private void openDeleteAlert(final Note note, final int adapterPos, final int layoutPos){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivityContext());
        alertBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNote(note, adapterPos, layoutPos);
            }
        });

        alertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertBuilder.setTitle("Delete Note");
        alertBuilder.setMessage("Delte " + note.getText() + "?");

        AlertDialog alertDialog = alertBuilder.create();
        try {
            getView().showAlert(alertDialog);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void deleteNote(final Note note, final int adapterPos, final int layoutPos){
        getView().showProgress();
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {
                return model.deleteNote(note, adapterPos);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                try {
                    getView().hideProgress();
                    if( result ){
                        getView().notifyItemRemoved(layoutPos);
                        getView().showToast(makeToast("Note Deleted"));
                    } else {
                        getView().showToast(makeToast("Error deleting note[" + note.getId()+"]"));
                    }
                } catch (NullPointerException e ){
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    @Override
    public void clickNewNote(EditText editText) {
        getView().showProgress();
        final String noteText = editText.getText().toString();
        if( !noteText.isEmpty()){
            new AsyncTask<Void, Void, Integer>(){

                @Override
                protected Integer doInBackground(Void... voids) {
                    return model.insertNote(makeNote(noteText));
                }

                @Override
                protected void onPostExecute(Integer adapterPosition) {
                    try {
                        if(adapterPosition > -1) {
                            getView().hideProgress();
                            getView().clearEditText();
                            getView().notifyItemInserted(adapterPosition +1 );
                            getView().notifyItemRangeChanged(adapterPosition, model.getNotesCount());
                        } else {
                            getView().hideProgress();
                            getView().showToast(makeToast("Error creating note [" + noteText + "]"));
                        }
                    } catch (NullPointerException e ){
                        e.printStackTrace();
                    }
                }
            }.execute();
        } else {
            try {
                getView().showToast(makeToast("cannot add a blank note!"));
            } catch (NullPointerException e ){
                e.printStackTrace();
            }
        }
    }

    private Note makeNote(String noteText) {
        Note note = new Note();
        note.setText(noteText);
        note.setDate(getDate());
        return note;
    }

    private String getDate() {
        return new SimpleDateFormat("HH:mm:ss - MM/dd/yyyy", Locale.getDefault()).format(new Date());
    }


    @Override
    public int getNotesCount() {
        return model.getNotesCount();
    }

    @Override
    public NotesViewHolder createViewHolder(ViewGroup parent, int viewType) {
        NotesViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewTaskRow = inflater.inflate(R.layout.note_view_holder, parent, false);
        viewHolder = new NotesViewHolder(viewTaskRow);

        return viewHolder;
    }

    @Override
    public void bindViewHolder(final NotesViewHolder holder, int position) {
        final Note note = model.getNote(position);
        holder.text.setText(note.getText());
        holder.date.setText(note.getDate());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDeleteNote(note, holder.getAdapterPosition(), holder.getLayoutPosition());
            }
        });
    }

    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        } catch (NullPointerException e){
            return null;
        }
    }

    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        } catch (NullPointerException e) {
            return null;
        }
    }
}
