package de.danoeh.antennapod.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobeta.android.dslv.DragSortListView;

import de.danoeh.antennapod.R;
import de.danoeh.antennapod.core.feed.FeedItem;
import de.danoeh.antennapod.core.feed.FeedMedia;
import de.danoeh.antennapod.core.feed.QueueEvent;
import de.danoeh.antennapod.core.storage.DBReader;
import de.danoeh.antennapod.core.storage.DBWriter;
import de.danoeh.antennapod.core.util.gui.FeedItemUndoToken;
import de.danoeh.antennapod.core.util.gui.UndoBarController;
import de.greenrobot.event.EventBus;


/**
 * Like 'EpisodesFragment' except that it only shows favorite episodes and
 * supports swiping to remove from favorites.
 */

public class FavoriteEpisodesFragment extends AllEpisodesFragment {

    public static final String TAG = "FavoriteEpisodesFrag";

    private static final String PREF_NAME = "PrefFavoriteEpisodesFragment";

    private UndoBarController undoBarController;

    public FavoriteEpisodesFragment() {
        super(false, PREF_NAME);
    }

    public void onEvent(QueueEvent event) {
        Log.d(TAG, "onEvent(" + event + ")");
        startItemLoader();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void resetViewState() {
        super.resetViewState();
        undoBarController = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateViewHelper(inflater, container, savedInstanceState,
                R.layout.episodes_fragment_with_undo);

        listView.setRemoveListener(which -> {
            Log.d(TAG, "remove(" + which + ")");
            stopItemLoader();
            FeedItem item = (FeedItem) listView.getAdapter().getItem(which);

            // TODO: actually remove the item from favorites

            undoBarController.showUndoBar(false,
                    getString(R.string.removed_from_favorites), new FeedItemUndoToken(item,
                            which)
            );

            throw new RuntimeException("can't remove yet");
        });

        undoBarController = new UndoBarController<FeedItemUndoToken>(root.findViewById(R.id.undobar), new UndoBarController.UndoListener<FeedItemUndoToken>() {

            private final Context context = getActivity();

            @Override
            public void onUndo(FeedItemUndoToken token) {
                if (token != null) {
                    long itemId = token.getFeedItemId();
                    // TODO: put it back DBWriter.markItemPlayed(FeedItem.NEW, itemId);
                    throw new RuntimeException("can't undo remove yet");
                }
            }

            @Override
            public void onHide(FeedItemUndoToken token) {
                // nothing to do
            }
        });
        return root;
    }

    @Override
    protected void startItemLoader() {
        if (itemLoader != null) {
            itemLoader.cancel(true);
        }
        itemLoader = new FavItemLoader();
        itemLoader.execute();
    }

    private class FavItemLoader extends AllEpisodesFragment.ItemLoader {

        @Override
        protected Object[] doInBackground(Void... params) {
            Context context = mainActivity.get();
            if (context != null) {
                return new Object[]{
                        DBReader.getFavoriteItemsList(),
                        DBReader.getQueueIDList()
                };
            } else {
                return null;
            }
        }
    }

}
