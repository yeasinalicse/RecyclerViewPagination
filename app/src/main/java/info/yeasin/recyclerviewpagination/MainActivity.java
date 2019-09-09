package info.yeasin.recyclerviewpagination;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import info.yeasin.recyclerviewpagination.model.ResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName();

    Context context;
    private RecyclerView mRecyclerView;
    private ItemAdapter mAdapter;
    private ProgressBar progressBar;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int page = 180;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            context = this;
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(linearLayoutManager);

            mAdapter = new ItemAdapter(context);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    if (!isLastPage) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadData(page);
                            }
                        }, 200);
                    }
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });

            mAdapter.setOnItemClicklListener(new ItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(context, "Clicked item position: " + position, Toast.LENGTH_LONG).show();
                }
            });

            loadData(page);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void loadData(final int page) {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.createService(ApiInterface.class);
        Call<ResponseData> call = apiService.getUsers(page);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                ResponseData serverResponse = response.body();
                Log.e("datasize", String.valueOf(serverResponse.getHits().size()));
                Log.e("pagesize", String.valueOf(page));
                resultAction(serverResponse);
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void resultAction(ResponseData model) {
        progressBar.setVisibility(View.INVISIBLE);
        isLoading = false;
        if (model != null) {
            mAdapter.addItems(model.getHits());
            if (model.getPage() == 200) {
                Log.e("datasize1", String.valueOf(model.getHits().size()));
                Log.e("pagesize1", String.valueOf(page));
                isLastPage = true;
            } else {
                page = page + 1;
            }
        }
    }
}
