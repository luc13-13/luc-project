package com.lc.framework.core;

import com.lc.framework.core.constants.CharConstants;
import com.lc.framework.core.constants.StringConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/16 14:59
 * @version : 1.0
 */
public class FileUtils {

    /**
     * 特殊后缀
     */
    private static final CharSequence[] SPECIAL_SUFFIX = {"tar.bz2", "tar.Z", "tar.gz", "tar.xz"};

    public static String getExtName(String filename) {
        if (filename == null) {
            return null;
        }
        final int index = filename.lastIndexOf(StringConstants.DOT);
        if (index == -1) {
            return StringConstants.EMPTY_STRING;
        } else {
            // issue#I4W5FS@Gitee
            final int secondToLastIndex = filename.substring(0, index).lastIndexOf(StringConstants.DOT);
            final String substr = filename.substring(secondToLastIndex == -1 ? index : secondToLastIndex + 1);
            if (StringUtils.containsAny(substr, SPECIAL_SUFFIX)) {
                return substr;
            }

            final String ext = filename.substring(index + 1);
            // 扩展名中不能包含路径相关的符号，Linux中是\，Windows中是\\
            return StringUtils.containsAny(ext, CharConstants.SLASH, CharConstants.BACKSLASH) ? StringConstants.EMPTY_STRING : ext;
        }
    }
}
