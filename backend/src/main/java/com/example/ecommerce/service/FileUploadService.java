package com.example.ecommerce.service;

import com.example.ecommerce.dto.FileUploadDTO;
import com.example.ecommerce.entity.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 文件上传服务接口
 */
public interface FileUploadService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @param fileType 文件类型
     * @param description 文件描述
     * @param tags 文件标签
     * @param createdBy 创建人ID
     * @param createdByName 创建人姓名
     * @return 文件上传记录
     */
    FileUpload uploadFile(MultipartFile file, String fileType, String description, String tags, Long createdBy, String createdByName);

    /**
     * 批量上传文件
     *
     * @param files 文件列表
     * @param fileType 文件类型
     * @param description 文件描述
     * @param tags 文件标签
     * @param createdBy 创建人ID
     * @param createdByName 创建人姓名
     * @return 文件上传记录列表
     */
    List<FileUpload> batchUploadFiles(List<MultipartFile> files, String fileType, String description, String tags, Long createdBy, String createdByName);

    /**
     * 根据ID获取文件上传记录
     *
     * @param id 文件上传记录ID
     * @return 文件上传记录
     */
    FileUpload getFileUploadById(Long id);

    /**
     * 根据存储文件名获取文件上传记录
     *
     * @param storedName 存储文件名
     * @return 文件上传记录
     */
    FileUpload getFileUploadByStoredName(String storedName);

    /**
     * 根据文件路径获取文件上传记录
     *
     * @param filePath 文件路径
     * @return 文件上传记录
     */
    FileUpload getFileUploadByFilePath(String filePath);

    /**
     * 根据文件哈希值获取文件上传记录
     *
     * @param fileHash 文件哈希值
     * @return 文件上传记录
     */
    FileUpload getFileUploadByFileHash(String fileHash);

    /**
     * 获取文件上传记录列表
     *
     * @return 文件上传记录列表
     */
    List<FileUpload> getFileUploadList();

    /**
     * 获取文件上传记录列表（分页）
     *
     * @param page 页码
     * @param size 每页大小
     * @return 文件上传记录列表
     */
    List<FileUpload> getFileUploadList(Integer page, Integer size);

    /**
     * 获取文件上传记录列表（带条件）
     *
     * @param fileType 文件类型
     * @param status 状态
     * @param createdBy 创建人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     * @param size 每页大小
     * @return 文件上传记录列表
     */
    List<FileUpload> getFileUploadList(String fileType, Integer status, Long createdBy, LocalDateTime startTime, LocalDateTime endTime, Integer page, Integer size);

    /**
     * 获取文件上传记录总数
     *
     * @param fileType 文件类型
     * @param status 状态
     * @param createdBy 创建人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 文件上传记录总数
     */
    int getFileUploadCount(String fileType, Integer status, Long createdBy, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 更新文件上传记录
     *
     * @param id 文件上传记录ID
     * @param fileUploadDTO 文件上传记录DTO
     * @return 更新后的文件上传记录
     */
    FileUpload updateFileUpload(Long id, FileUploadDTO fileUploadDTO);

    /**
     * 删除文件上传记录
     *
     * @param id 文件上传记录ID
     */
    void deleteFileUpload(Long id);

    /**
     * 根据存储文件名删除文件上传记录
     *
     * @param storedName 存储文件名
     */
    void deleteFileUploadByStoredName(String storedName);

    /**
     * 根据文件路径删除文件上传记录
     *
     * @param filePath 文件路径
     */
    void deleteFileUploadByFilePath(String filePath);

    /**
     * 根据文件哈希值删除文件上传记录
     *
     * @param fileHash 文件哈希值
     */
    void deleteFileUploadByFileHash(String fileHash);

    /**
     * 根据创建人ID删除文件上传记录
     *
     * @param createdBy 创建人ID
     */
    void deleteFileUploadByCreatedBy(Long createdBy);

    /**
     * 根据文件类型删除文件上传记录
     *
     * @param fileType 文件类型
     */
    void deleteFileUploadByFileType(String fileType);

    /**
     * 批量删除文件上传记录
     *
     * @param ids 文件上传记录ID列表
     */
    void batchDeleteFileUploads(List<Long> ids);

    /**
     * 增加下载次数
     *
     * @param id 文件上传记录ID
     */
    void increaseDownloadCount(Long id);

    /**
     * 增加分享次数
     *
     * @param id 文件上传记录ID
     */
    void increaseShareCount(Long id);

    /**
     * 增加备份次数
     *
     * @param id 文件上传记录ID
     */
    void increaseBackupCount(Long id);

    /**
     * 更新文件状态
     *
     * @param id 文件上传记录ID
     * @param status 状态
     */
    void updateFileStatus(Long id, Integer status);

    /**
     * 更新文件审核状态
     *
     * @param id 文件上传记录ID
     * @param auditStatus 审核状态
     * @param auditorId 审核人ID
     * @param auditorName 审核人姓名
     * @param auditTime 审核时间
     * @param auditOpinion 审核意见
     */
    void updateFileAuditStatus(Long id, Integer auditStatus, Long auditorId, String auditorName, LocalDateTime auditTime, String auditOpinion);

    /**
     * 更新文件过期时间
     *
     * @param id 文件上传记录ID
     * @param expireAt 过期时间
     */
    void updateFileExpireTime(Long id, LocalDateTime expireAt);

    /**
     * 更新文件分享过期时间
     *
     * @param id 文件上传记录ID
     * @param shareExpireAt 分享过期时间
     */
    void updateFileShareExpireTime(Long id, LocalDateTime shareExpireAt);

    /**
     * 更新文件备份过期时间
     *
     * @param id 文件上传记录ID
     * @param backupExpireAt 备份过期时间
     */
    void updateFileBackupExpireTime(Long id, LocalDateTime backupExpireAt);

    /**
     * 获取文件访问URL
     *
     * @param id 文件上传记录ID
     * @return 文件访问URL
     */
    String getFileUrl(Long id);

    /**
     * 获取文件预览URL
     *
     * @param id 文件上传记录ID
     * @return 文件预览URL
     */
    String getPreviewUrl(Long id);

    /**
     * 获取文件缩略图URL
     *
     * @param id 文件上传记录ID
     * @return 文件缩略图URL
     */
    String getThumbnailUrl(Long id);

    /**
     * 下载文件
     *
     * @param id 文件上传记录ID
     * @return 文件字节数组
     */
    byte[] downloadFile(Long id);

    /**
     * 预览文件
     *
     * @param id 文件上传记录ID
     * @return 文件字节数组
     */
    byte[] previewFile(Long id);

    /**
     * 获取文件统计信息
     *
     * @return 文件统计信息
     */
    Map<String, Object> getFileUploadStats();

    /**
     * 获取文件类型统计信息
     *
     * @return 文件类型统计信息
     */
    List<Map<String, Object>> getFileUploadTypeStats();

    /**
     * 获取文件时间统计信息
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 文件时间统计信息
     */
    List<Map<String, Object>> getFileUploadTimeStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取文件用户统计信息
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 文件用户统计信息
     */
    List<Map<String, Object>> getFileUploadUserStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取文件大小分布统计信息
     *
     * @return 文件大小分布统计信息
     */
    List<Map<String, Object>> getFileUploadSizeDistribution();

    /**
     * 获取即将过期的文件列表
     *
     * @param days 天数
     * @return 即将过期的文件列表
     */
    List<FileUpload> getExpiringFiles(Integer days);

    /**
     * 获取已过期的文件列表
     *
     * @return 已过期的文件列表
     */
    List<FileUpload> getExpiredFiles();

    /**
     * 获取需要清理的文件列表
     *
     * @return 需要清理的文件列表
     */
    List<FileUpload> getFilesToClean();

    /**
     * 获取热门文件列表
     *
     * @param limit 限制数量
     * @return 热门文件列表
     */
    List<FileUpload> getPopularFiles(Integer limit);

    /**
     * 获取大文件列表
     *
     * @param size 大小（字节）
     * @param limit 限制数量
     * @return 大文件列表
     */
    List<FileUpload> getLargeFiles(Long size, Integer limit);

    /**
     * 获取小文件列表
     *
     * @param size 大小（字节）
     * @param limit 限制数量
     * @return 小文件列表
     */
    List<FileUpload> getSmallFiles(Long size, Integer limit);

    /**
     * 获取最近上传的文件列表
     *
     * @param limit 限制数量
     * @return 最近上传的文件列表
     */
    List<FileUpload> getRecentFiles(Integer limit);

    /**
     * 获取待审核的文件列表
     *
     * @param limit 限制数量
     * @return 待审核的文件列表
     */
    List<FileUpload> getFilesToAudit(Integer limit);

    /**
     * 获取已拒绝的文件列表
     *
     * @param limit 限制数量
     * @return 已拒绝的文件列表
     */
    List<FileUpload> getRejectedFiles(Integer limit);

    /**
     * 获取已通过的文件列表
     *
     * @param limit 限制数量
     * @return 已通过的文件列表
     */
    List<FileUpload> getApprovedFiles(Integer limit);

    /**
     * 获取文件综合统计信息
     *
     * @return 文件综合统计信息
     */
    Map<String, Object> getFileUploadComprehensiveStats();

    /**
     * 批量更新文件状态
     *
     * @param ids 文件上传记录ID列表
     * @param status 状态
     */
    void batchUpdateFileStatus(List<Long> ids, Integer status);

    /**
     * 批量更新文件审核状态
     *
     * @param ids 文件上传记录ID列表
     * @param auditStatus 审核状态
     * @param auditorId 审核人ID
     * @param auditorName 审核人姓名
     * @param auditTime 审核时间
     * @param auditOpinion 审核意见
     */
    void batchUpdateFileAuditStatus(List<Long> ids, Integer auditStatus, Long auditorId, String auditorName, LocalDateTime auditTime, String auditOpinion);

    /**
     * 批量更新文件过期时间
     *
     * @param ids 文件上传记录ID列表
     * @param expireAt 过期时间
     */
    void batchUpdateFileExpireTime(List<Long> ids, LocalDateTime expireAt);

    /**
     * 批量更新文件分享过期时间
     *
     * @param ids 文件上传记录ID列表
     * @param shareExpireAt 分享过期时间
     */
    void batchUpdateFileShareExpireTime(List<Long> ids, LocalDateTime shareExpireAt);

    /**
     * 批量更新文件备份过期时间
     *
     * @param ids 文件上传记录ID列表
     * @param backupExpireAt 备份过期时间
     */
    void batchUpdateFileBackupExpireTime(List<Long> ids, LocalDateTime backupExpireAt);

    /**
     * 清理过期文件
     *
     * @return 清理的文件数量
     */
    int cleanExpiredFiles();

    /**
     * 清理需要清理的文件
     *
     * @return 清理的文件数量
     */
    int cleanFiles();

    /**
     * 备份文件
     *
     * @param id 文件上传记录ID
     * @return 备份是否成功
     */
    boolean backupFile(Long id);

    /**
     * 批量备份文件
     *
     * @param ids 文件上传记录ID列表
     * @return 备份成功的文件数量
     */
    int batchBackupFiles(List<Long> ids);

    /**
     * 压缩文件
     *
     * @param id 文件上传记录ID
     * @return 压缩是否成功
     */
    boolean compressFile(Long id);

    /**
     * 批量压缩文件
     *
     * @param ids 文件上传记录ID列表
     * @return 压缩成功的文件数量
     */
    int batchCompressFiles(List<Long> ids);

    /**
     * 加密文件
     *
     * @param id 文件上传记录ID
     * @param encryptionAlgorithm 加密算法
     * @param encryptionKey 加密密钥
     * @param encryptionIv 加密IV
     * @return 加密是否成功
     */
    boolean encryptFile(Long id, String encryptionAlgorithm, String encryptionKey, String encryptionIv);

    /**
     * 批量加密文件
     *
     * @param ids 文件上传记录ID列表
     * @param encryptionAlgorithm 加密算法
     * @param encryptionKey 加密密钥
     * @param encryptionIv 加密IV
     * @return 加密成功的文件数量
     */
    int batchEncryptFiles(List<Long> ids, String encryptionAlgorithm, String encryptionKey, String encryptionIv);

    /**
     * 解密文件
     *
     * @param id 文件上传记录ID
     * @param encryptionAlgorithm 加密算法
     * @param encryptionKey 加密密钥
     * @param encryptionIv 加密IV
     * @return 解密是否成功
     */
    boolean decryptFile(Long id, String encryptionAlgorithm, String encryptionKey, String encryptionIv);

    /**
     * 批量解密文件
     *
     * @param ids 文件上传记录ID列表
     * @param encryptionAlgorithm 加密算法
     * @param encryptionKey 加密密钥
     * @param encryptionIv 加密IV
     * @return 解密成功的文件数量
     */
    int batchDecryptFiles(List<Long> ids, String encryptionAlgorithm, String encryptionKey, String encryptionIv);

    /**
     * 生成文件分享链接
     *
     * @param id 文件上传记录ID
     * @param expireDays 有效期（天）
     * @param shareLimit 分享次数限制
     * @param sharePassword 分享密码
     * @return 分享链接
     */
    String generateShareLink(Long id, Integer expireDays, Integer shareLimit, String sharePassword);

    /**
     * 验证文件分享链接
     *
     * @param shareId 分享ID
     * @param sharePassword 分享密码
     * @return 文件上传记录
     */
    FileUpload validateShareLink(String shareId, String sharePassword);

    /**
     * 获取文件分享信息
     *
     * @param shareId 分享ID
     * @return 文件分享信息
     */
    Map<String, Object> getShareInfo(String shareId);

    /**
     * 获取文件分享列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 文件分享列表
     */
    List<Map<String, Object>> getShareList(Integer page, Integer size);

    /**
     * 删除文件分享
     *
     * @param shareId 分享ID
     */
    void deleteShare(String shareId);

    /**
     * 批量删除文件分享
     *
     * @param shareIds 分享ID列表
     */
    void batchDeleteShares(List<String> shareIds);

    /**
     * 获取文件分享统计信息
     *
     * @return 文件分享统计信息
     */
    Map<String, Object> getShareStats();

    /**
     * 获取文件分享时间统计信息
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 文件分享时间统计信息
     */
    List<Map<String, Object>> getShareTimeStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取文件分享用户统计信息
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 文件分享用户统计信息
     */
    List<Map<String, Object>> getShareUserStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取文件分享文件统计信息
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 文件分享文件统计信息
     */
    List<Map<String, Object>> getShareFileStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取文件分享综合统计信息
     *
     * @return 文件分享综合统计信息
     */
    Map<String, Object> getShareComprehensiveStats();

    /**
     * 检查文件是否存在
     *
     * @param id 文件上传记录ID
     * @return 文件是否存在
     */
    boolean fileExists(Long id);

    /**
     * 检查文件是否可访问
     *
     * @param id 文件上传记录ID
     * @return 文件是否可访问
     */
    boolean fileAccessible(Long id);

    /**
     * 检查文件是否可下载
     *
     * @param id 文件上传记录ID
     * @return 文件是否可下载
     */
    boolean fileDownloadable(Long id);

    /**
     * 检查文件是否可预览
     *
     * @param id 文件上传记录ID
     * @return 文件是否可预览
     */
    boolean filePreviewable(Long id);

    /**
     * 检查文件是否可分享
     *
     * @param id 文件上传记录ID
     * @return 文件是否可分享
     */
    boolean fileSharable(Long id);

    /**
     * 检查文件是否过期
     *
     * @param id 文件上传记录ID
     * @return 文件是否过期
     */
    boolean fileExpired(Long id);

    /**
     * 检查文件是否即将过期
     *
     * @param id 文件上传记录ID
     * @param days 天数
     * @return 文件是否即将过期
     */
    boolean fileExpiring(Long id, Integer days);

    /**
     * 检查文件是否需要审核
     *
     * @param id 文件上传记录ID
     * @return 文件是否需要审核
     */
    boolean fileNeedsAudit(Long id);

    /**
     * 检查文件是否已审核
     *
     * @param id 文件上传记录ID
     * @return 文件是否已审核
     */
    boolean fileAudited(Long id);

    /**
     * 检查文件是否已通过审核
     *
     * @param id 文件上传记录ID
     * @return 文件是否已通过审核
     */
    boolean fileApproved(Long id);

    /**
     * 检查文件是否已拒绝审核
     *
     * @param id 文件上传记录ID
     * @return 文件是否已拒绝审核
     */
    boolean fileRejected(Long id);

    /**
     * 检查文件是否已备份
     *
     * @param id 文件上传记录ID
     * @return 文件是否已备份
     */
    boolean fileBackedUp(Long id);

    /**
     * 检查文件是否已压缩
     *
     * @param id 文件上传记录ID
     * @return 文件是否已压缩
     */
    boolean fileCompressed(Long id);

    /**
     * 检查文件是否已加密
     *
     * @param id 文件上传记录ID
     * @return 文件是否已加密
     */
    boolean fileEncrypted(Long id);

    /**
     * 检查文件是否已分享
     *
     * @param id 文件上传记录ID
     * @return 文件是否已分享
     */
    boolean fileShared(Long id);

    /**
     * 检查文件是否已下载
     *
     * @param id 文件上传记录ID
     * @return 文件是否已下载
     */
    boolean fileDownloaded(Long id);

    /**
     * 检查文件是否已预览
     *
     * @param id 文件上传记录ID
     * @return 文件是否已预览
     */
    boolean filePreviewed(Long id);

    /**
     * 检查文件权限
     *
     * @param id 文件上传记录ID
     * @param permission 权限类型
     * @return 是否有权限
     */
    boolean checkFilePermission(Long id, String permission);

    /**
     * 检查文件分享权限
     *
     * @param shareId 分享ID
     * @param sharePassword 分享密码
     * @param permission 权限类型
     * @return 是否有权限
     */
    boolean checkSharePermission(String shareId, String sharePassword, String permission);

    /**
     * 获取文件权限信息
     *
     * @param id 文件上传记录ID
     * @return 文件权限信息
     */
    Map<String, Object> getFilePermissionInfo(Long id);

    /**
     * 获取文件分享权限信息
     *
     * @param shareId 分享ID
     * @param sharePassword 分享密码
     * @return 文件分享权限信息
     */
    Map<String, Object> getSharePermissionInfo(String shareId, String sharePassword);

    /**
     * 设置文件权限
     *
     * @param id 文件上传记录ID
     * @param permission 权限类型
     * @param value 权限值
     */
    void setFilePermission(Long id, String permission, Integer value);

    /**
     * 设置文件分享权限
     *
     * @param shareId 分享ID
     * @param sharePassword 分享密码
     * @param permission 权限类型
     * @param value 权限值
     */
    void setSharePermission(String shareId, String sharePassword, String permission, Integer value);

    /**
     * 获取文件元数据
     *
     * @param id 文件上传记录ID
     * @return 文件元数据
     */
    Map<String, Object> getFileMetadata(Long id);

    /**
     * 设置文件元数据
     *
     * @param id 文件上传记录ID
     * @param metadata 元数据
     */
    void setFileMetadata(Long id, Map<String, Object> metadata);

    /**
     * 获取文件标签
     *
     * @param id 文件上传记录ID
     * @return 文件标签列表
     */
    List<String> getFileTags(Long id);

    /**
     * 设置文件标签
     *
     * @param id 文件上传记录ID
     * @param tags 标签列表
     */
    void setFileTags(Long id, List<String> tags);

    /**
     * 添加文件标签
     *
     * @param id 文件上传记录ID
     * @param tag 标签
     */
    void addFileTag(Long id, String tag);

    /**
     * 删除文件标签
     *
     * @param id 文件上传记录ID
     * @param tag 标签
     */
    void removeFileTag(Long id, String tag);

    /**
     * 获取文件备注
     *
     * @param id 文件上传记录ID
     * @return 文件备注
     */
    String getFileRemarks(Long id);

    /**
     * 设置文件备注
     *
     * @param id 文件上传记录ID
     * @param remarks 备注
     */
    void setFileRemarks(Long id, String remarks);

    /**
     * 获取文件版本历史
     *
     * @param id 文件上传记录ID
     * @return 文件版本历史列表
     */
    List<FileUpload> getFileVersionHistory(Long id);

    /**
     * 创建文件版本
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @param description 版本描述
     * @return 新版本文件上传记录
     */
    FileUpload createFileVersion(Long id, String version, String description);

    /**
     * 恢复文件版本
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 是否恢复成功
     */
    boolean restoreFileVersion(Long id, String version);

    /**
     * 删除文件版本
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     */
    void deleteFileVersion(Long id, String version);

    /**
     * 获取文件版本列表
     *
     * @param id 文件上传记录ID
     * @return 文件版本列表
     */
    List<FileUpload> getFileVersionList(Long id);

    /**
     * 获取文件版本统计信息
     *
     * @param id 文件上传记录ID
     * @return 文件版本统计信息
     */
    Map<String, Object> getFileVersionStats(Long id);

    /**
     * 获取文件版本时间统计信息
     *
     * @param id 文件上传记录ID
     * @return 文件版本时间统计信息
     */
    List<Map<String, Object>> getFileVersionTimeStats(Long id);

    /**
     * 获取文件版本大小统计信息
     *
     * @param id 文件上传记录ID
     * @return 文件版本大小统计信息
     */
    List<Map<String, Object>> getFileVersionSizeStats(Long id);

    /**
     * 获取文件版本综合统计信息
     *
     * @param id 文件上传记录ID
     * @return 文件版本综合统计信息
     */
    Map<String, Object> getFileVersionComprehensiveStats(Long id);

    /**
     * 检查文件版本是否存在
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否存在
     */
    boolean fileVersionExists(Long id, String version);

    /**
     * 检查文件版本是否可访问
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否可访问
     */
    boolean fileVersionAccessible(Long id, String version);

    /**
     * 检查文件版本是否可下载
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否可下载
     */
    boolean fileVersionDownloadable(Long id, String version);

    /**
     * 检查文件版本是否可预览
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否可预览
     */
    boolean fileVersionPreviewable(Long id, String version);

    /**
     * 检查文件版本是否可分享
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否可分享
     */
    boolean fileVersionSharable(Long id, String version);

    /**
     * 检查文件版本是否过期
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否过期
     */
    boolean fileVersionExpired(Long id, String version);

    /**
     * 检查文件版本是否即将过期
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @param days 天数
     * @return 文件版本是否即将过期
     */
    boolean fileVersionExpiring(Long id, String version, Integer days);

    /**
     * 检查文件版本是否需要审核
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否需要审核
     */
    boolean fileVersionNeedsAudit(Long id, String version);

    /**
     * 检查文件版本是否已审核
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否已审核
     */
    boolean fileVersionAudited(Long id, String version);

    /**
     * 检查文件版本是否已通过审核
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否已通过审核
     */
    boolean fileVersionApproved(Long id, String version);

    /**
     * 检查文件版本是否已拒绝审核
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否已拒绝审核
     */
    boolean fileVersionRejected(Long id, String version);

    /**
     * 检查文件版本是否已备份
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否已备份
     */
    boolean fileVersionBackedUp(Long id, String version);

    /**
     * 检查文件版本是否已压缩
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否已压缩
     */
    boolean fileVersionCompressed(Long id, String version);

    /**
     * 检查文件版本是否已加密
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否已加密
     */
    boolean fileVersionEncrypted(Long id, String version);

    /**
     * 检查文件版本是否已分享
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否已分享
     */
    boolean fileVersionShared(Long id, String version);

    /**
     * 检查文件版本是否已下载
     *
     * @param id 文件上传记录ID
     * @param version 版本号
     * @return 文件版本是否已下载
     */
    boolean fileVersionDownloaded(Long id, String version);

    /**
}