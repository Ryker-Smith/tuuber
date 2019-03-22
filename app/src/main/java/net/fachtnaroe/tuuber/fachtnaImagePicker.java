package net.fachtnaroe.tuuber;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.ActivityResultListener;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.Picker;
import com.google.appinventor.components.runtime.util.MediaUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

@DesignerComponent(
        version = 5,
        description = "A special-purpose button. When the user taps an image picker, the device's image gallery appears, and the user can choose an image. After an image is picked, it is saved, and the <code>Selected</code> property will be the name of the file where the image is stored. In order to not fill up storage, a maximum of 10 images will be stored.  Picking more images will delete previous images, in order from oldest to newest.",
        category = ComponentCategory.MEDIA
)
@UsesPermissions(
        permissionNames = "android.permission.WRITE_EXTERNAL_STORAGE"
)
@SimpleObject
public class fachtnaImagePicker extends Picker implements ActivityResultListener {
    private static final String LOG_TAG = "ImagePicker";
    private static final String imagePickerDirectoryName = "/Pictures/_app_inventor_image_picker";
    private static final String FILE_PREFIX = "picked_image";
    private static int maxSavedFiles = 10;
    private String selectionURI;
    private String selectionSavedImage = "";
    tuuberCommonSubroutines tools=new tuuberCommonSubroutines(this.container);

    public fachtnaImagePicker(ComponentContainer container) {
        super(container);
    }

    @SimpleProperty(
            description = "Path to the file containing the image that was selected.",
            category = PropertyCategory.BEHAVIOR
    )
    public String Selection() {
        return this.selectionSavedImage;
    }

    protected Intent getIntent() {
        return new Intent("android.intent.action.PICK", Media.INTERNAL_CONTENT_URI);
    }

    public void resultReturned(int requestCode, int resultCode, Intent data) {
        tools.dbg(data.toString());
        if (requestCode == this.requestCode && resultCode == -1) {
            Uri selectedImage = data.getData();
            this.selectionURI = selectedImage.toString();
            Log.i("ImagePicker", "selectionURI = " + this.selectionURI);
            ContentResolver cR = this.container.$context().getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String extension = "." + mime.getExtensionFromMimeType(cR.getType(selectedImage));
            Log.i("ImagePicker", "extension = " + extension);
            this.saveSelectedImageToExternalStorage(extension);
            this.AfterPicking();
        }

    }

    private void saveSelectedImageToExternalStorage(String extension) {
        this.selectionSavedImage = "";
        File tempFile = null;

        try {
            tempFile = MediaUtil.copyMediaToTempFile(this.container.$form(), this.selectionURI);
        } catch (IOException var4) {
            Log.i("ImagePicker", "copyMediaToTempFile failed: " + var4.getMessage());
            this.container.$form().dispatchErrorOccurredEvent(this, "ImagePicker", 1602, new Object[]{var4.getMessage()});
            return;
        }

        Log.i("ImagePicker", "temp file path is: " + tempFile.getPath());
        this.copyToExternalStorageAndDeleteSource(tempFile, extension);
    }

    private void copyToExternalStorageAndDeleteSource(File source, String extension) {
        File dest = null;
        InputStream inStream = null;
        OutputStream outStream = null;
        String fullDirname = Environment.getExternalStorageDirectory() + "/Pictures/_app_inventor_image_picker";
        File destDirectory = new File(fullDirname);

        try {
            destDirectory.mkdirs();
            dest = File.createTempFile("picked_image", extension, destDirectory);
            this.selectionSavedImage = dest.getPath();
            Log.i("ImagePicker", "saved file path is: " + this.selectionSavedImage);
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];

            int length;
            while((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

            inStream.close();
            outStream.close();
            Log.i("ImagePicker", "Image was copied to " + this.selectionSavedImage);
        } catch (IOException var10) {
            String err = "destination is " + this.selectionSavedImage + ": " + "error is " + var10.getMessage();
            Log.i("ImagePicker", "copyFile failed. " + err);
            this.container.$form().dispatchErrorOccurredEvent(this, "SaveImage", 1601, new Object[]{err});
            this.selectionSavedImage = "";
            dest.delete();
        }

        source.delete();
        this.trimDirectory(maxSavedFiles, destDirectory);
    }

    private void trimDirectory(int maxSavedFiles, File directory) {
        File[] files = directory.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            }
        });
        int excess = files.length - maxSavedFiles;

        for(int i = 0; i < excess; ++i) {
            files[i].delete();
        }

    }
}
