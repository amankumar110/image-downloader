package in.amankumar110.imagedownloaderapp.utils;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.Map;

import javax.inject.Inject;

public class StorageManager {

    private ActivityResultLauncher<String[]> permissionLauncher;

    @Inject
    public StorageManager() {

    }

    public boolean checkStoragePermission(Context context) {
        return ActivityCompat.checkSelfPermission(context,WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public void initiate(Fragment fragment, ActivityResultCallback<Map<String,Boolean>> resultCallback) {
        this.permissionLauncher = fragment
                .registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                        resultCallback);
    }

    public void requestStoragePermission() {
        permissionLauncher.launch(new String[]{WRITE_EXTERNAL_STORAGE});
    }
}
