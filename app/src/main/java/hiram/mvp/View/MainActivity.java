package hiram.mvp.View;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import hiram.mvp.Model.MainModel;
import hiram.mvp.MainMVP;
import hiram.mvp.Presenter.MainPresenter;
import hiram.mvp.R;
import hiram.mvp.View.recycler.NotesViewHolder;
import hiram.mvp.common.StateMaintainer;

public class MainActivity extends AppCompatActivity implements MainMVP.RequiredView, View.OnClickListener {

    private MainMVP.ProvidedPresenter presenter;
    private EditText etNote;
    private ListNotes listAdapter;
    private ProgressBar progressBar;

    //Responsable de mantener la integridad del objeto durante cambios
  //  private final StateMaintainer stateMaintainer = new StateMaintainer( getSupportFragmentManager()
  //          , MainActivity.class.getName());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupMVP();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy(isChangingConfigurations());
    }

    /*
    setup
    the
    views
     */

    private void setupViews(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        etNote = findViewById(R.id.et_newNote);
        listAdapter = new ListNotes();
        progressBar = findViewById(R.id.progressBar);

        RecyclerView list = findViewById(R.id.recycler_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        list.setLayoutManager(layoutManager);
        list.setAdapter(listAdapter);
        list.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupMVP(){
        //check if statemanager has been created
      //  if(stateMaintainer.firstItemIn()) {
            //create presenter
            MainPresenter mainPresenter = new MainPresenter(this);
            //Create model
            MainModel mainModel = new MainModel(mainPresenter);
            //set presenter model
            mainPresenter.setModel(mainModel);
            //add presenter and model to StateMaintainer
      //      stateMaintainer.put(mainPresenter);
      //      stateMaintainer.put(mainModel);

            presenter = mainPresenter;
        //} else {
       //     presenter = stateMaintainer.get(MainPresenter.class.getName());
            //update view
            presenter.setView(this);
        }
    //}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab: {
                presenter.clickNewNote(etNote);
            }
        }
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void showToast(Toast toast) {
        toast.show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showAlert(AlertDialog dialog) {
        dialog.show();
    }

    @Override
    public void notifyItemRemoved(int position) {
        listAdapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyDataSetChanged() {
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearEditText() {
        etNote.setText("");
    }

    @Override
    public void notifyItemInserted(int layoutPosition) {
        listAdapter.notifyItemInserted(layoutPosition);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        listAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    private class ListNotes extends RecyclerView.Adapter<NotesViewHolder>{

        @NonNull
        @Override
        public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return presenter.createViewHolder(viewGroup, i);
        }

        @Override
        public void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, int i) {
            presenter.bindViewHolder(notesViewHolder, i);
        }

        @Override
        public int getItemCount() {
            return presenter.getNotesCount();
        }
    }

}
