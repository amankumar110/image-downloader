package in.amankumar110.imagedownloaderapp.paging;

import static in.amankumar110.imagedownloaderapp.utils.Constants.API_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import java.util.List;

import javax.inject.Inject;

import in.amankumar110.imagedownloaderapp.dao.ImageApi;
import in.amankumar110.imagedownloaderapp.models.Image;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ImagePagingSource extends RxPagingSource<Integer, Image> {

    private final ImageApi imageApi;

    @Inject
    public ImagePagingSource(ImageApi imageApi) {
        this.imageApi = imageApi;
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Image> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Image>> loadSingle(@NonNull LoadParams<Integer> loadParams) {

        try {
            int page = loadParams.getKey() == null ? 1 : loadParams.getKey();


            return imageApi.getImages(API_KEY, page)
                    .subscribeOn(Schedulers.io())
                    .map(result -> toLoadResult(page, result))
                    .onErrorReturn(LoadResult.Error::new);
        } catch (RuntimeException e) {

            RuntimeException exception = new RuntimeException("Something went wrong, try again");

            return Single.just(new LoadResult.Error<>(exception));
        }
    }

    private LoadResult<Integer,Image> toLoadResult(int page, List<Image> images) {

        return new LoadResult.Page<>(images,page==1?null:page-1,page+1);
    }
}
