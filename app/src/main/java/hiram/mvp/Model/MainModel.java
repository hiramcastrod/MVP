package hiram.mvp.Model;

import java.util.ArrayList;

import hiram.mvp.MainMVP;
import hiram.mvp.data.DAO;
import hiram.mvp.models.Note;

public class MainModel implements MainMVP.ProvidedModel {
    //References
    private MainMVP.RequiredPresenter requiredPresenter;
    private DAO dao;
    public ArrayList<Note> notes;

    public MainModel(MainMVP.RequiredPresenter presenter){
        this.requiredPresenter = presenter;
        dao = new DAO(this.requiredPresenter.getAppContext());
    }

    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if(!isChangingConfiguration){
            requiredPresenter = null;
            dao = null;
            notes =null;
        }
    }

    @Override
    public int getNotesCount() {
        if( notes != null){
            return notes.size();
        }
        return 0;
    }

    @Override
    public Note getNote(int position) {
        return notes.get(position);
    }

    public int getNotePosition(Note note){
        for(int i = 0 ; i<notes.size(); i++){
            if(note.getId()== notes.get(i).getId()){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int insertNote(Note note) {
        Note insertedNote = dao.insertNote(note);
        if( insertedNote != null){
            loadData();
            return getNotePosition(insertedNote);
        }
        return -1;
    }

    @Override
    public boolean loadData() {
        notes = dao.getAllNotes();
        return notes != null;
    }

    @Override
    public boolean deleteNote(Note note, int adapterPos) {
        long res = dao.deleteNote(note);
        if(res > 0){
            notes.remove(adapterPos);
            return true;
        }
        return false;
    }
}
