package hiram.mvp.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class StateMaintainer {

    protected final String TAG = getClass().getSimpleName();
    private final String mStateMaintenerTag;
    private final WeakReference<FragmentManager> mFragmentManager;
    private StateMngFragment mStateMaintainerFrag;
    private boolean mIsRecreating;

    public StateMaintainer(FragmentManager fragmentManager, String stateMaintenerTAG) {
        mFragmentManager = new WeakReference<>(fragmentManager);
        mStateMaintenerTag = stateMaintenerTAG;
    }

    public boolean firstItemIn(){
        try{
            mStateMaintainerFrag = new StateMngFragment();
            mFragmentManager.get().findFragmentByTag(mStateMaintenerTag);

            if(mStateMaintainerFrag == null){
                Log.d(TAG, "Nuevo Retained Fragment" + mStateMaintenerTag);
                mStateMaintainerFrag = new StateMngFragment();
                mFragmentManager.get().beginTransaction().add(mStateMaintainerFrag,
                        mStateMaintenerTag).commit();
                mIsRecreating = false;
                return true;
            } else {
                Log.d(TAG, "Regresando fragment existente " + mStateMaintenerTag);
                mIsRecreating = true;
                return false;
            }
        } catch (NullPointerException e) {
            Log.w(TAG, "Error firstTimeIn()");
            return false;
        }
    }

    public boolean wasRecreated(){
        return mIsRecreating;
    }

    public void put(String key, Object object){
        mStateMaintainerFrag.put(key, object);
    }

    public void put(Object obj) {
        put(obj.getClass().getName(), obj);
    }

    public <T> T get(String key)  {
        return mStateMaintainerFrag.get(key);
    }

    public boolean hasKey(String key) {
        return mStateMaintainerFrag.get(key) != null;
    }

    public static class StateMngFragment extends Fragment {
        private HashMap<String, Object> mData = new HashMap<>();

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        //Garantiza que el fragment se preserve
            setRetainInstance(true);
        }

        public void put(String key, Object object){
            mData.put(key, object);
        }

        public void put(Object object){
            put(object.getClass().getName(), object);
        }

        public <T> T get(String key){
            return (T) mData.get(key);
        }
    }
}
