package hiram.mvp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import hiram.mvp.View.recycler.NotesViewHolder;
import hiram.mvp.models.Note;

public interface MainMVP {

    interface RequiredView {
        Context getAppContext();
        Context getActivityContext();
        void showToast(Toast toast);
        void showProgress();
        void hideProgress();
        void showAlert(AlertDialog dialog);
        void notifyItemRemoved(int position);
        void notifyDataSetChanged();
        void clearEditText();
        void notifyItemInserted(int layoutPosition);
        void notifyItemRangeChanged(int positionStart, int itemCount);
    }

    interface ProvidedPresenter {
        void onDestroy(boolean insChangingConfiguration);
        void setView(RequiredView view);
        void clickDeleteNote(Note note, int adapterPos, int layoutPos);
        void clickNewNote(EditText editText);
        int getNotesCount();
        NotesViewHolder createViewHolder(ViewGroup parent, int viewType);
        void bindViewHolder(NotesViewHolder holder, int position);
    }

    interface RequiredPresenter {
        Context getAppContext();
        Context getActivityContext();
    }

    interface ProvidedModel {
        void onDestroy(boolean isChangingConfiguration);
        int getNotesCount();
        Note getNote(int position);
        int insertNote(Note note);
        boolean loadData();
        boolean deleteNote(Note note, int adapterPos);
    }
}
