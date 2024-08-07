package in.amankumar110.imagedownloaderapp.viewmodel;

import android.widget.ImageView;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import in.amankumar110.imagedownloaderapp.models.Image;
import in.amankumar110.imagedownloaderapp.paging.ImagePagingSource;
import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class ImageViewModel extends ViewModel {

    private Flowable<PagingData<Image>> imagesFlowable;
    private final ImagePagingSource imagePagingSource;

    @Inject
    public ImageViewModel(ImagePagingSource imagePagingSource) {
        this.imagePagingSource = imagePagingSource;
        initialize();
    }

    public void initialize() {

        Pager<Integer,Image> pager = new Pager<>(new PagingConfig(
                10,
                10,
                true,
                10,
                10*200
        ), ()-> imagePagingSource);

        imagesFlowable = PagingRx.getFlowable(pager);
        CoroutineScope scope = ViewModelKt.getViewModelScope(this);
        PagingRx.cachedIn(imagesFlowable,scope);

    }

    public Flowable<PagingData<Image>> getImagesFlowable() {
        return imagesFlowable;
    }

    public void setImagesFlowable(Flowable<PagingData<Image>> imagesFlowable) {
        this.imagesFlowable = imagesFlowable;
    }

}
