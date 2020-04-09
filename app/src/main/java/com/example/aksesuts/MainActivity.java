package com.example.aksesuts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aksesuts.data.Cheese;
import com.example.aksesuts.provider.SampleContentProvider;


    /**
     * Not very relevant to Room. This just shows data from {@link SampleContentProvider}.
     *
     * <p>Since the data is exposed through the ContentProvider, other apps can read and write the
     * content in a similar manner to this.</p>
     */
    public class MainActivity extends AppCompatActivity {

        private static final int LOADER_CHEESES = 1;

        private CheeseAdapter mCheeseAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) { // onStart asalnya diganti dengan onCreate
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main); // Ganti nama (nama layout awalnya salah)

            final RecyclerView list = findViewById(R.id.list); //tambahan agar R terbaca
            list.setLayoutManager(new LinearLayoutManager(list.getContext()));
            mCheeseAdapter = new CheeseAdapter();
            list.setAdapter(mCheeseAdapter);

            LoaderManager.getInstance(this).initLoader(LOADER_CHEESES, null, mLoaderCallbacks);
        }

        private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
                new LoaderManager.LoaderCallbacks<Cursor>() {

                    @Override
                    @NonNull
                    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                        return new CursorLoader(getApplicationContext(),
                                SampleContentProvider.URI_CHEESE,
                                new String[]{Cheese.COLUMN_NAME},
                                null, null, null);
                    }

                    @Override
                    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                        mCheeseAdapter.setCheeses(data);
                    }

                    @Override
                    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                        mCheeseAdapter.setCheeses(null);
                    }

                };

        private static class CheeseAdapter extends RecyclerView.Adapter<CheeseAdapter.ViewHolder> {

            private Cursor mCursor;

            @Override
            @NonNull
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(parent);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                if (mCursor.moveToPosition(position)) {
                    holder.mText.setText(mCursor.getString(
                            mCursor.getColumnIndexOrThrow(Cheese.COLUMN_NAME)));
                }
            }


            @Override
            public int getItemCount() {

                return mCursor == null ? 0 : mCursor.getCount();
            }


            void setCheeses(Cursor cursor) {
                mCursor = cursor;
                notifyDataSetChanged();
            }

            static class ViewHolder extends RecyclerView.ViewHolder {

                final TextView mText;

                ViewHolder(ViewGroup parent) {
                    super(LayoutInflater.from(parent.getContext()).inflate(
                            android.R.layout.simple_list_item_1, parent, false));
                    mText = itemView.findViewById(android.R.id.text1);
                }

            }

        }

    }

