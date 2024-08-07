package in.amankumar110.imagedownloaderapp.dao;


import java.util.List;

import in.amankumar110.imagedownloaderapp.models.Image;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImageApi {

    @GET("/photos")
    Single<List<Image>> getImages(@Query("client_id") String apiKey,
                                  @Query("page") int page);

}
