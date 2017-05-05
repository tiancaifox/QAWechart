package com.nutcracker.wedo.common.attachment.service.impl;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.nutcracker.wedo.common.attachment.bo.Attachment;
import com.nutcracker.wedo.common.attachment.dao.FileRepoDao;
import com.nutcracker.wedo.common.attachment.service.FileRepoService;
import com.nutcracker.wedo.common.attachment.service.FileTokenProvider;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 附件服务实现
 * Created by huh on 2017/4/18.
 */
@Service("fileRepoService")
public class FileRepoServiceImpl implements FileRepoService{

    @Resource(name = "fileTokenProvider")
    private FileTokenProvider fileTokenProvider;

    @Resource(name = "fileRepoDao")
    private FileRepoDao fileRepoDao;

    @Override
    public Long save(final InputStream stream, Attachment attachment) {
        String fileName = UploadUtils.generateFilename("",
                Files.getFileExtension(attachment.getName()));
        String absolutePath = AttachmentConstant.MFS_PATH + fileName;
        try {
            File dest = new File(absolutePath);
            FileUtils.touch(dest);
            // 存儲附件
            Files.copy(new InputSupplier<InputStream>() {
                @Override
                public InputStream getInput() throws IOException {
                    return stream;
                }
            }, dest);
            stream.close();
        } catch (IOException e) {
            ExceptionUtil.wrapException("附件保存失败", e, FileRepoServiceImpl.class);
        }
        attachment.setPath(fileName);
        return insertMeta(attachment);
    }

    @Override
    public Long downloadAndSave(String url, String category) {

        final InputStream inStream ;
        String ext = "";
        String fileName = UploadUtils.generateFilename("", ext);
        String path = AttachmentConstant.MFS_PATH + File.pathSeparator + category + File.pathSeparator + fileName;
        try {
            URL httpUrl = new URL(url);
            URLConnection conn = httpUrl.openConnection();
            inStream = conn.getInputStream();
            File dest = new File(path);
            FileUtils.touch(dest);
            // 存儲附件
            Files.copy(new InputSupplier<InputStream>() {
                @Override
                public InputStream getInput() throws IOException {
                    return inStream;
                }
            }, dest);
            inStream.close();
        } catch (ClientErrorException e) {
            ExceptionUtil.wrapBusinessException("http访问失败", e, FileRepoServiceImpl.class);
        } catch (IOException e) {
            ExceptionUtil.wrapBusinessException("文件存储失败", e, FileRepoServiceImpl.class);
        }
        Attachment attachment = new Attachment();
        attachment.setName(fileName);
        attachment.setPath(path);
        return insertMeta(attachment);
    }

    @Override
    public String escapeForHttp(String text, String attachmentCategory) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        StringBuffer sb = new StringBuffer(text);
        // 循環url表達式
        Pattern pattern = Pattern.compile(AttachmentConstant.TRANSFERRED_IMG_PATTERN);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            // 訪問http接口下載附件
            String url = matcher.group(1);
            Long id = downloadAndSave(url, attachmentCategory);
            replace(sb, url, id);
        }
        return sb.toString();
    }

    @Override
    public String escape(String html) {
        if (StringUtils.isBlank(html)) {
            return "";
        }
        for (IRichTextFilter rtFilter : RichTextFilterFactory.getInstence().getFilters()) {
            html = rtFilter.doFilter(html);
        }
        return html;
    }

    @Override
    public String render(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        Pattern pattern = Pattern.compile(AttachmentConstant.FID_PATTERN_STRING);
        Matcher matcher = pattern.matcher(text);
        int start = 0;
        int end = 0;
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            start = matcher.start();
            sb.append(text.substring(end, start));
            end = matcher.end();
            String group = matcher.group();
            Long fid = Long.parseLong(group.substring(3, group.length() - 3));
            String downloadUrl = fileTokenProvider.getDownLoadUrl(fid);
            sb.append(downloadUrl);
        }
        if (sb.length() == 0) {
            return text;
        }
        sb.append(text.substring(end));
        return sb.toString();
    }

    @Override
    public Attachment readMetaByToken(String token) {
        Attachment attachment = fileRepoDao.readMetaByToken(token);
        attachment.setDownloadUrl(AttachmentConstant.DOWNLOAD_URL + attachment.getToken());
        return attachment;
    }

    @Override
    public Attachment readMeta(Long id) {
        Attachment attachment = new Attachment();
        attachment = fileRepoDao.readMetaById(id);
        attachment.setDownloadUrl(AttachmentConstant.DOWNLOAD_URL + attachment.getToken());
        return attachment;
    }

    @Override
    public Map<Long, Attachment> batchReadMeta(List<Long> ids) {
        Map<Long, Attachment> metas = new HashMap<Long, Attachment>();
        if (CollectionUtils.isEmpty(ids)) {
            return metas;
        }
        List<Attachment> metaList = this.fileRepoDao.batchReadMeta(ids);
        for (Attachment m : metaList) {
            m.setDownloadUrl(AttachmentConstant.DOWNLOAD_URL + m.getToken());
            metas.put(m.getId(), m);
        }
        return metas;
    }

    /**
     * 插入附件条目信息
     *
     * @param attachment 文件
     * @return 附件条目ID
     */
    protected Long insertMeta(Attachment attachment) {
        attachment.setToken(Hashing.md5().newHasher().putString(UUID.randomUUID().toString()).hash().toString());
        fileRepoDao.saveAttachment(attachment);
        return attachment.getId();
    }
    /**
     * 替换指定的字符串为附件标示
     *
     * @param sb 字符串缓冲区
     * @param str 目标字符串
     * @param id 需要替换成目的字符串
     */
    protected void replace(StringBuffer sb, String str, Long id) {
        // 替換附件ID
        String newChar = "###" + id + "###";
        String richTextTmp = StringUtils.replace(sb.toString(), str, newChar);
        sb.setLength(0);
        sb.append(richTextTmp);
    }
}
