package in.amankumar110.imagedownloaderapp;

import static in.amankumar110.imagedownloaderapp.utils.Constants.ERROR_MESSAGE;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.CombinedLoadStates;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.RequestManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import in.amankumar110.imagedownloaderapp.databinding.FragmentMainBinding;
import in.amankumar110.imagedownloaderapp.models.Image;
import in.amankumar110.imagedownloaderapp.utils.AlertHelper;
import in.amankumar110.imagedownloaderapp.utils.Constants;
import in.amankumar110.imagedownloaderapp.utils.Helper;
import in.amankumar110.imagedownloaderapp.viewmodel.ImageViewModel;
import io.reactivex.rxjava3.disposables.Disposable;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

@AndroidEntryPoint
public class MainFragment extends Fragment {

    @Inject
    RequestManager glide;
    private FragmentMainBinding binding;
    private ImageViewModel imageViewModel;
    private ImageAdapter imageAdapter;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        Helper.setupSystemBar(requireActivity());
        initializeRecyclerView();
        loadImages();


    }

    private void loadImages() {
        Disposable disposable = imageViewModel.getImagesFlowable()
                .subscribe(data -> imageAdapter.submitData(getLifecycle(), data));

        imageAdapter.addLoadStateListener(combinedLoadStates -> {

            LoadState refreshState = combinedLoadStates.getRefresh();

            if(refreshState instanceof LoadState.Error) {

                AlertHelper.setTitle("Network Error!");
                AlertHelper.setMessage(ERROR_MESSAGE);
                AlertHelper.setNegativeButtonText("Exit");
                AlertHelper.setPositiveButtonText("Retry");
                AlertHelper.setNegativeButtonCallback((dialog,which) -> {
                    requireActivity().finish();
                });
                AlertHelper.setPositiveButtonCallback((dialog,which) -> {
                    imageAdapter.retry();
                });
                AlertHelper.showAlert(requireContext());

            }

            return Unit.INSTANCE;
        });
    }

    private void initializeRecyclerView() {

        imageAdapter = new ImageAdapter(glide, this::onImageClicked);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false);
        binding.rvImages.setLayoutManager(layoutManager);

        // Combine the ImageAdapter with the LoadStateAdapter
        binding.rvImages.setAdapter(imageAdapter.withLoadStateFooter(
                new ImagesLoadStateAdapter(view -> imageAdapter.retry())
        ));

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // Calculate the total item count, including the footer
                int itemCount = imageAdapter.getItemCount() + 1; // +1 for the LoadStateAdapter footer

                // Check if this position corresponds to the LoadStateAdapter footer
                if (position == itemCount - 1) {
                    // Footer should take up the full span width
                    return layoutManager.getSpanCount();
                } else {
                    // Regular items take up one span
                    return 1;
                }
            }
        });
    }


    private void onImageClicked(Image image) {

            Bundle data = new Bundle();
            data.putString("url", image.getUrls().getRegular());
            navController.navigate(R.id.action_mainFragment_to_imageFragment, data);

    }


}