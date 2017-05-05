package com.nutcracker.wedo.common.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 上传工具类
 * Created by huh on 2017/5/4.
 */
public class UploadUtils {

    /**
     * 62个字母和数字，含大小写
     */
    public static final char[] N62_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z' };
    /**
     * 日期格式化对象
     */
    public static final DateFormat MONTH_FORMAT = new SimpleDateFormat("/yyyy/MM/dd/HHmmss");

    /**
     * 生成文件名
     *
     * @param path 文件路径
     * @param ext 文件扩展名
     * @return 包含文件路径存储文件路径
     */
    public static String generateFilename(String path, String ext) {
        return path + MONTH_FORMAT.format(new Date()) + RandomStringUtils.random(4, N62_CHARS) + "." + ext;
    }

    /**
     * 路径格式
     */
    protected static final Pattern ILLEGAL_CURRENT_FOLDER_PATTERN = Pattern
            .compile("^[^/]|[^/]$|/\\.{1,2}|\\\\|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}");

    /**
     * Sanitizes a filename from certain chars.<br />
     *
     * This method enforces the <code>forceSingleExtension</code> property and then replaces all occurrences of \, /, |,
     * :, ?, *, &quot;, &lt;, &gt;, control chars by _ (underscore).
     *
     * @param filename a potentially 'malicious' filename
     * @return sanitized filename
     */
    public static String sanitizeFileName(final String filename) {

        if (StringUtils.isBlank(filename)) {
            return filename;
        }

        String name = forceSingleExtension(filename);

        // Remove \ / | : ? * " < > 'Control Chars' with _
        return name.replaceAll("\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
    }

    /**
     * Sanitizes a folder name from certain chars.<br />
     *
     * This method replaces all occurrences of \, /, |, :, ?, *, &quot;, &lt;, &gt;, control chars by _ (underscore).
     *
     * @param folderName a potentially 'malicious' folder name
     * @return sanitized folder name
     */
    public static String sanitizeFolderName(final String folderName) {

        if (StringUtils.isBlank(folderName)) {
            return folderName;
        }

        // Remove . \ / | : ? * " < > 'Control Chars' with _
        return folderName.replaceAll("\\.|\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
    }

    /**
     * Checks whether a path complies with the FCKeditor File Browser <a
     * href="http://docs.fckeditor.net/FCKeditor_2.x/Developers_Guide/Server_Side_Integration#File_Browser_Requests"
     * target="_blank">rules</a>.
     *
     * @param path a potentially 'malicious' path
     * @return <code>true</code> if path complies with the rules, else <code>false</code>
     */
    public static boolean isValidPath(final String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }

        if (ILLEGAL_CURRENT_FOLDER_PATTERN.matcher(path).find()) {
            return false;
        }

        return true;
    }

    /**
     * Replaces all dots in a filename with underscores except the last one.
     *
     * @param filename filename to sanitize
     * @return string with a single dot only
     */
    public static String forceSingleExtension(final String filename) {
        return filename.replaceAll("\\.(?![^.]+$)", "_");
    }

    /**
     * Checks if a filename contains more than one dot.
     *
     * @param filename filename to check
     * @return <code>true</code> if filename contains severals dots, else <code>false</code>
     */
    public static boolean isSingleExtension(final String filename) {
        return filename.matches("[^\\.]+\\.[^\\.]+");
    }

    /**
     * Checks a directory for existence and creates it if non-existent.
     *
     * @param dir directory to check/create
     */
    public static void checkDirAndCreate(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Iterates over a base name and returns the first non-existent file.<br />
     * This method extracts a file's base name, iterates over it until the first non-existent appearance with
     * <code>basename(n).ext</code>. Where n is a positive integer starting from one.
     *
     * @param file base file
     * @return first non-existent file
     */
    public static File getUniqueFile(final File file) {
        if (!file.exists()) {
            return file;
        }

        File tmpFile = new File(file.getAbsolutePath());
        File parentDir = tmpFile.getParentFile();
        int count = 1;
        String extension = FilenameUtils.getExtension(tmpFile.getName());
        String baseName = FilenameUtils.getBaseName(tmpFile.getName());
        do {
            tmpFile = new File(parentDir, baseName + "(" + count++ + ")." + extension);
        } while (tmpFile.exists());
        return tmpFile;
    }

}

