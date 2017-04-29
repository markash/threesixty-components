package io.threesixty.ui.component.uploader;

import java.io.File;

/**
 * @author Mark P Ashworth
 */
@FunctionalInterface
public interface UploadSucceededListener {
    void onUpload(final File file, final String mimeType, final long length);
}
