package info.yeasin.recyclerviewpagination;

import info.yeasin.recyclerviewpagination.model.ResponseData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("?key=13564212-a55bf9e8842c8a92ccc2c5c38&q=yellow+flowers&image_type=photo&")
    Call<ResponseData> getUsers(@Query("per_page") int page);
}
