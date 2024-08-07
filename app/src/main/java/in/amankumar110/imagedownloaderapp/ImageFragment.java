package in.amankumar110.imagedownloaderapp;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static in.amankumar110.imagedownloaderapp.utils.Constants.IMAGE_FAILED_MESSAGE;

import android.app.DownloadManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import in.amankumar110.imagedownloaderapp.databinding.FragmentImageBinding;
import in.amankumar110.imagedownloaderapp.utils.Helper;
import in.amankumar110.imagedownloaderapp.utils.StorageManager;

@AndroidEntryPoint
public class ImageFragment extends Fragment {

    private FragmentImageBinding binding;
    private String url;
    private DownloadManager downloadManager;
    private NavController navController;
    @Inject
    RequestManager glide;
    @Inject
    StorageManager storageManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if storage permission is needed based on the API level
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            storageManager.initiate(this, this::onStoragePermissionResponse);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentImageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        downloadManager = requireContext().getSystemService(DownloadManager.class);

        Helper.setupSystemBar(requireActivity());
        loadUrl();
        loadImage();
        binding.btnDownload.setOnClickListener(this::onDownloadButtonClicked);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate back to FragmentMain
                navController.navigate(R.id.action_imageFragment_to_mainFragment);
            }
        });
    }

    private void loadUrl() {
        if (getArguments() != null)
            url = getArguments().getString("url");
    }

    private void loadImage() {

        glide.load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(requireContext(), IMAGE_FAILED_MESSAGE, Toast.LENGTH_SHORT).show();
                binding.btnDownload.setEnabled(false);
                binding.btnDownload.setAlpha(0.5f);
                return false;
            }

            @Override
            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {

                return false;
            }
        }).into(binding.image);
    }

    private void onStoragePermissionResponse(Map<String, Boolean> result) {
        Log.v("permission", result + "");
        if (Boolean.TRUE.equals(result.get(WRITE_EXTERNAL_STORAGE)))
            downloadImage();
    }

    private void onDownloadButtonClicked(View v) {
        // If API level is Android 9 (API level 28) or below, check for storage permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (storageManager.checkStoragePermission(requireContext())) {
                downloadImage();
            } else {
                storageManager.requestStoragePermission();
            }
        } else {
            // For Android 10+ (API level 29 and above), directly download the image
            downloadImage();
        }
    }

    private void downloadImage() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                .setTitle("image.jpeg");

        // Set the destination directory based on the API level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "image.jpeg");
        } else {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "image.jpeg");
        }

        downloadManager.enqueue(request);
    }
}
