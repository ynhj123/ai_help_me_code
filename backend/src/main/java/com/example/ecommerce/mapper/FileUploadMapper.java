package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.FileUpload;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 文件上传Mapper接口
 */
@Mapper
public interface FileUploadMapper {

    /**
     * 插入文件上传记录
     *
     * @param fileUpload 文件上传记录
     * @return 影响行数
     */
    @Insert({
        "<script>",
        "INSERT INTO file_uploads (original_name, stored_name, file_path, file_size, mime_type, file_type, file_url, file_hash, status, created_by, created_by_name, description, tags, download_count, preview_url, thumbnail_url, width, height, duration, bitrate, format, version, permission, expire_days, expire_at, allow_download, allow_preview, allow_share, share_password, share_limit, share_count, share_expire_at, ip_address, user_agent, upload_source, storage_location, storage_bucket, storage_region, storage_path, access_key, secret_key, encrypted, encryption_algorithm, encryption_key, encryption_iv, checksum, checksum_algorithm, backup, backup_path, backup_count, backup_expire_at, compressed, compression_algorithm, compression_ratio, original_file_size, version_number, parent_id, child_count, level, sort_order, path, full_path, extension, icon, color, tag_color, remarks, audit_status, auditor_id, auditor_name, audit_time, audit_opinion, created_at, updated_at)",
        "VALUES (#{originalName}, #{storedName}, #{filePath}, #{fileSize}, #{mimeType}, #{fileType}, #{fileUrl}, #{fileHash}, #{status}, #{createdBy}, #{createdByName}, #{description}, #{tags}, #{downloadCount}, #{previewUrl}, #{thumbnailUrl}, #{width}, #{height}, #{duration}, #{bitrate}, #{format}, #{version}, #{permission}, #{expireDays}, #{expireAt}, #{allowDownload}, #{allowPreview}, #{allowShare}, #{sharePassword}, #{shareLimit}, #{shareCount}, #{shareExpireAt}, #{ipAddress}, #{userAgent}, #{uploadSource}, #{storageLocation}, #{storageBucket}, #{storageRegion}, #{storagePath}, #{accessKey}, #{secretKey}, #{encrypted}, #{encryptionAlgorithm}, #{encryptionKey}, #{encryptionIv}, #{checksum}, #{checksumAlgorithm}, #{backup}, #{backupPath}, #{backupCount}, #{backupExpireAt}, #{compressed}, #{compressionAlgorithm}, #{compressionRatio}, #{originalFileSize}, #{versionNumber}, #{parentId}, #{childCount}, #{level}, #{sortOrder}, #{path}, #{fullPath}, #{extension}, #{icon}, #{color}, #{tagColor}, #{remarks}, #{auditStatus}, #{auditorId}, #{auditorName}, #{auditTime}, #{auditOpinion}, #{createdAt}, #{updatedAt})",
        "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FileUpload fileUpload);

    /**
     * 批量插入文件上传记录
     *
     * @param fileUploads 文件上传记录列表
     * @return 影响行数
     */
    int batchInsert(@Param("fileUploads") List<FileUpload> fileUploads);

    /**
     * 根据ID查询文件上传记录
     *
     * @param id 文件上传记录ID
     * @return 文件上传记录
     */
    @Select("SELECT * FROM file_uploads WHERE id = #{id}")
    FileUpload selectById(Long id);

    /**
     * 根据存储文件名查询文件上传记录
     *
     * @param storedName 存储文件名
     * @return 文件上传记录
     */
    @Select("SELECT * FROM file_uploads WHERE stored_name = #{storedName}")
    FileUpload selectByStoredName(String storedName);

    /**
     * 根据文件路径查询文件上传记录
     *
     * @param filePath 文件路径
     * @return 文件上传记录
     */
    @Select("SELECT * FROM file_uploads WHERE file_path = #{filePath}")
    FileUpload selectByFilePath(String filePath);

    /**
     * 根据文件哈希值查询文件上传记录
     *
     * @param fileHash 文件哈希值
     * @return 文件上传记录
     */
    @Select("SELECT * FROM file_uploads WHERE file_hash = #{fileHash}")
    FileUpload selectByFileHash(String fileHash);

    /**
     * 查询文件上传记录列表
     *
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads ORDER BY id DESC")
    List<FileUpload> selectAll();

    /**
     * 查询文件上传记录列表（分页）
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads ORDER BY id DESC LIMIT #{offset}, #{limit}")
    List<FileUpload> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询文件上传记录列表（带条件）
     *
     * @param fileType 文件类型
     * @param status   状态
     * @param createdBy 创建人ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param offset    偏移量
     * @param limit     限制数量
     * @return 文件上传记录列表
     */
    @Select({
        "<script>",
        "SELECT * FROM file_uploads WHERE 1=1",
        "<if test='fileType != null and fileType != \"\"'>",
        " AND file_type = #{fileType}",
        "</if>",
        "<if test='status != null'>",
        " AND status = #{status}",
        "</if>",
        "<if test='createdBy != null'>",
        " AND created_by = #{createdBy}",
        "</if>",
        "<if test='startTime != null'>",
        " AND created_at >= #{startTime}",
        "</if>",
        "<if test='endTime != null'>",
        " AND created_at <= #{endTime}",
        "</if>",
        "ORDER BY id DESC",
        "LIMIT #{offset}, #{limit}",
        "</script>"
    })
    List<FileUpload> selectAllWithConditions(
            @Param("fileType") String fileType,
            @Param("status") Integer status,
            @Param("createdBy") Long createdBy,
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime,
            @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * 查询文件上传记录总数
     *
     * @param fileType 文件类型
     * @param status   状态
     * @param createdBy 创建人ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 文件上传记录总数
     */
    @Select({
        "<script>",
        "SELECT COUNT(*) FROM file_uploads WHERE 1=1",
        "<if test='fileType != null and fileType != \"\"'>",
        " AND file_type = #{fileType}",
        "</if>",
        "<if test='status != null'>",
        " AND status = #{status}",
        "</if>",
        "<if test='createdBy != null'>",
        " AND created_by = #{createdBy}",
        "</if>",
        "<if test='startTime != null'>",
        " AND created_at >= #{startTime}",
        "</if>",
        "<if test='endTime != null'>",
        " AND created_at <= #{endTime}",
        "</if>",
        "</script>"
    })
    int count(
            @Param("fileType") String fileType,
            @Param("status") Integer status,
            @Param("createdBy") Long createdBy,
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime);

    /**
     * 更新文件上传记录
     *
     * @param fileUpload 文件上传记录
     * @return 影响行数
     */
    @Update({
        "<script>",
        "UPDATE file_uploads",
        "<set>",
        "<if test='originalName != null'>original_name = #{originalName},</if>",
        "<if test='storedName != null'>stored_name = #{storedName},</if>",
        "<if test='filePath != null'>file_path = #{filePath},</if>",
        "<if test='fileSize != null'>file_size = #{fileSize},</if>",
        "<if test='mimeType != null'>mime_type = #{mimeType},</if>",
        "<if test='fileType != null'>file_type = #{fileType},</if>",
        "<if test='fileUrl != null'>file_url = #{fileUrl},</if>",
        "<if test='fileHash != null'>file_hash = #{fileHash},</if>",
        "<if test='status != null'>status = #{status},</if>",
        "<if test='createdBy != null'>created_by = #{createdBy},</if>",
        "<if test='createdByName != null'>created_by_name = #{createdByName},</if>",
        "<if test='description != null'>description = #{description},</if>",
        "<if test='tags != null'>tags = #{tags},</if>",
        "<if test='downloadCount != null'>download_count = #{downloadCount},</if>",
        "<if test='previewUrl != null'>preview_url = #{previewUrl},</if>",
        "<if test='thumbnailUrl != null'>thumbnail_url = #{thumbnailUrl},</if>",
        "<if test='width != null'>width = #{width},</if>",
        "<if test='height != null'>height = #{height},</if>",
        "<if test='duration != null'>duration = #{duration},</if>",
        "<if test='bitrate != null'>bitrate = #{bitrate},</if>",
        "<if test='format != null'>format = #{format},</if>",
        "<if test='version != null'>version = #{version},</if>",
        "<if test='permission != null'>permission = #{permission},</if>",
        "<if test='expireDays != null'>expire_days = #{expireDays},</if>",
        "<if test='expireAt != null'>expire_at = #{expireAt},</if>",
        "<if test='allowDownload != null'>allow_download = #{allowDownload},</if>",
        "<if test='allowPreview != null'>allow_preview = #{allowPreview},</if>",
        "<if test='allowShare != null'>allow_share = #{allowShare},</if>",
        "<if test='sharePassword != null'>share_password = #{sharePassword},</if>",
        "<if test='shareLimit != null'>share_limit = #{shareLimit},</if>",
        "<if test='shareCount != null'>share_count = #{shareCount},</if>",
        "<if test='shareExpireAt != null'>share_expire_at = #{shareExpireAt},</if>",
        "<if test='ipAddress != null'>ip_address = #{ipAddress},</if>",
        "<if test='userAgent != null'>user_agent = #{userAgent},</if>",
        "<if test='uploadSource != null'>upload_source = #{uploadSource},</if>",
        "<if test='storageLocation != null'>storage_location = #{storageLocation},</if>",
        "<if test='storageBucket != null'>storage_bucket = #{storageBucket},</if>",
        "<if test='storageRegion != null'>storage_region = #{storageRegion},</if>",
        "<if test='storagePath != null'>storage_path = #{storagePath},</if>",
        "<if test='accessKey != null'>access_key = #{accessKey},</if>",
        "<if test='secretKey != null'>secret_key = #{secretKey},</if>",
        "<if test='encrypted != null'>encrypted = #{encrypted},</if>",
        "<if test='encryptionAlgorithm != null'>encryption_algorithm = #{encryptionAlgorithm},</if>",
        "<if test='encryptionKey != null'>encryption_key = #{encryptionKey},</if>",
        "<if test='encryptionIv != null'>encryption_iv = #{encryptionIv},</if>",
        "<if test='checksum != null'>checksum = #{checksum},</if>",
        "<if test='checksumAlgorithm != null'>checksum_algorithm = #{checksumAlgorithm},</if>",
        "<if test='backup != null'>backup = #{backup},</if>",
        "<if test='backupPath != null'>backup_path = #{backupPath},</if>",
        "<if test='backupCount != null'>backup_count = #{backupCount},</if>",
        "<if test='backupExpireAt != null'>backup_expire_at = #{backupExpireAt},</if>",
        "<if test='compressed != null'>compressed = #{compressed},</if>",
        "<if test='compressionAlgorithm != null'>compression_algorithm = #{compressionAlgorithm},</if>",
        "<if test='compressionRatio != null'>compression_ratio = #{compressionRatio},</if>",
        "<if test='originalFileSize != null'>original_file_size = #{originalFileSize},</if>",
        "<if test='versionNumber != null'>version_number = #{versionNumber},</if>",
        "<if test='parentId != null'>parent_id = #{parentId},</if>",
        "<if test='childCount != null'>child_count = #{childCount},</if>",
        "<if test='level != null'>level = #{level},</if>",
        "<if test='sortOrder != null'>sort_order = #{sortOrder},</if>",
        "<if test='path != null'>path = #{path},</if>",
        "<if test='fullPath != null'>full_path = #{fullPath},</if>",
        "<if test='extension != null'>extension = #{extension},</if>",
        "<if test='icon != null'>icon = #{icon},</if>",
        "<if test='color != null'>color = #{color},</if>",
        "<if test='tagColor != null'>tag_color = #{tagColor},</if>",
        "<if test='remarks != null'>remarks = #{remarks},</if>",
        "<if test='auditStatus != null'>audit_status = #{auditStatus},</if>",
        "<if test='auditorId != null'>auditor_id = #{auditorId},</if>",
        "<if test='auditorName != null'>auditor_name = #{auditorName},</if>",
        "<if test='auditTime != null'>audit_time = #{auditTime},</if>",
        "<if test='auditOpinion != null'>audit_opinion = #{auditOpinion},</if>",
        "updated_at = #{updatedAt}",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int update(FileUpload fileUpload);

    /**
     * 根据ID删除文件上传记录
     *
     * @param id 文件上传记录ID
     * @return 影响行数
     */
    @Delete("DELETE FROM file_uploads WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 根据存储文件名删除文件上传记录
     *
     * @param storedName 存储文件名
     * @return 影响行数
     */
    @Delete("DELETE FROM file_uploads WHERE stored_name = #{storedName}")
    int deleteByStoredName(String storedName);

    /**
     * 根据文件路径删除文件上传记录
     *
     * @param filePath 文件路径
     * @return 影响行数
     */
    @Delete("DELETE FROM file_uploads WHERE file_path = #{filePath}")
    int deleteByFilePath(String filePath);

    /**
     * 根据文件哈希值删除文件上传记录
     *
     * @param fileHash 文件哈希值
     * @return 影响行数
     */
    @Delete("DELETE FROM file_uploads WHERE file_hash = #{fileHash}")
    int deleteByFileHash(String fileHash);

    /**
     * 根据创建人ID删除文件上传记录
     *
     * @param createdBy 创建人ID
     * @return 影响行数
     */
    @Delete("DELETE FROM file_uploads WHERE created_by = #{createdBy}")
    int deleteByCreatedBy(Long createdBy);

    /**
     * 根据文件类型删除文件上传记录
     *
     * @param fileType 文件类型
     * @return 影响行数
     */
    @Delete("DELETE FROM file_uploads WHERE file_type = #{fileType}")
    int deleteByFileType(String fileType);

    /**
     * 检查文件上传记录是否存在
     *
     * @param id 文件上传记录ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM file_uploads WHERE id = #{id}")
    boolean existsById(Long id);

    /**
     * 检查文件上传记录是否存在
     *
     * @param storedName 存储文件名
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM file_uploads WHERE stored_name = #{storedName}")
    boolean existsByStoredName(String storedName);

    /**
     * 检查文件上传记录是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM file_uploads WHERE file_path = #{filePath}")
    boolean existsByFilePath(String filePath);

    /**
     * 检查文件上传记录是否存在
     *
     * @param fileHash 文件哈希值
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM file_uploads WHERE file_hash = #{fileHash}")
    boolean existsByFileHash(String fileHash);

    /**
     * 增加下载次数
     *
     * @param id 文件上传记录ID
     * @return 影响行数
     */
    @Update("UPDATE file_uploads SET download_count = download_count + 1, updated_at = NOW() WHERE id = #{id}")
    int increaseDownloadCount(Long id);

    /**
     * 增加分享次数
     *
     * @param id 文件上传记录ID
     * @return 影响行数
     */
    @Update("UPDATE file_uploads SET share_count = share_count + 1, updated_at = NOW() WHERE id = #{id}")
    int increaseShareCount(Long id);

    /**
     * 增加备份次数
     *
     * @param id 文件上传记录ID
     * @return 影响行数
     */
    @Update("UPDATE file_uploads SET backup_count = backup_count + 1, updated_at = NOW() WHERE id = #{id}")
    int increaseBackupCount(Long id);

    /**
     * 更新文件状态
     *
     * @param id     文件上传记录ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE file_uploads SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新文件审核状态
     *
     * @param id         文件上传记录ID
     * @param auditStatus 审核状态
     * @param auditorId  审核人ID
     * @param auditorName 审核人姓名
     * @param auditTime  审核时间
     * @param auditOpinion 审核意见
     * @return 影响行数
     */
    @Update("UPDATE file_uploads SET audit_status = #{auditStatus}, auditor_id = #{auditorId}, auditor_name = #{auditorName}, audit_time = #{auditTime}, audit_opinion = #{auditOpinion}, updated_at = NOW() WHERE id = #{id}")
    int updateAuditStatus(
            @Param("id") Long id,
            @Param("auditStatus") Integer auditStatus,
            @Param("auditorId") Long auditorId,
            @Param("auditorName") String auditorName,
            @Param("auditTime") java.time.LocalDateTime auditTime,
            @Param("auditOpinion") String auditOpinion);

    /**
     * 更新文件过期时间
     *
     * @param id       文件上传记录ID
     * @param expireAt 过期时间
     * @return 影响行数
     */
    @Update("UPDATE file_uploads SET expire_at = #{expireAt}, updated_at = NOW() WHERE id = #{id}")
    int updateExpireTime(@Param("id") Long id, @Param("expireAt") java.time.LocalDateTime expireAt);

    /**
     * 更新文件分享过期时间
     *
     * @param id           文件上传记录ID
     * @param shareExpireAt 分享过期时间
     * @return 影响行数
     */
    @Update("UPDATE file_uploads SET share_expire_at = #{shareExpireAt}, updated_at = NOW() WHERE id = #{id}")
    int updateShareExpireTime(@Param("id") Long id, @Param("shareExpireAt") java.time.LocalDateTime shareExpireAt);

    /**
     * 更新文件备份过期时间
     *
     * @param id             文件上传记录ID
     * @param backupExpireAt 备份过期时间
     * @return 影响行数
     */
    @Update("UPDATE file_uploads SET backup_expire_at = #{backupExpireAt}, updated_at = NOW() WHERE id = #{id}")
    int updateBackupExpireTime(@Param("id") Long id, @Param("backupExpireAt") java.time.LocalDateTime backupExpireAt);

    /**
     * 查询文件上传记录统计
     *
     * @return 文件上传记录统计
     */
    @Select("SELECT COUNT(*) as total_files, SUM(file_size) as total_size, AVG(file_size) as avg_size, MAX(file_size) as max_size, MIN(file_size) as min_size FROM file_uploads WHERE status = 1")
    java.util.Map<String, Object> findFileUploadStats();

    /**
     * 查询文件上传记录分类统计
     *
     * @return 文件上传记录分类统计
     */
    @Select("SELECT file_type, COUNT(*) as file_count, SUM(file_size) as total_size FROM file_uploads WHERE status = 1 GROUP BY file_type ORDER BY file_count DESC")
    java.util.List<java.util.Map<String, Object>> findFileUploadTypeStats();

    /**
     * 查询文件上传记录时间统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 文件上传记录时间统计
     */
    @Select("SELECT DATE(created_at) as date, COUNT(*) as file_count, SUM(file_size) as total_size FROM file_uploads WHERE created_at BETWEEN #{startTime} AND #{endTime} GROUP BY DATE(created_at) ORDER BY date")
    java.util.List<java.util.Map<String, Object>> findFileUploadTimeStats(@Param("startTime") java.time.LocalDateTime startTime, @Param("endTime") java.time.LocalDateTime endTime);

    /**
     * 查询文件上传记录用户统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 文件上传记录用户统计
     */
    @Select("SELECT created_by, created_by_name, COUNT(*) as file_count, SUM(file_size) as total_size FROM file_uploads WHERE created_at BETWEEN #{startTime} AND #{endTime} GROUP BY created_by, created_by_name ORDER BY file_count DESC LIMIT 10")
    java.util.List<java.util.Map<String, Object>> findFileUploadUserStats(@Param("startTime") java.time.LocalDateTime startTime, @Param("endTime") java.time.LocalDateTime endTime);

    /**
     * 查询文件上传记录大小分布
     *
     * @return 文件上传记录大小分布
     */
    @Select({
        "<script>",
        "SELECT ",
        "CASE ",
        "  WHEN file_size < 1024 THEN '< 1KB' ",
        "  WHEN file_size BETWEEN 1024 AND 10240 THEN '1KB-10KB' ",
        "  WHEN file_size BETWEEN 10240 AND 102400 THEN '10KB-100KB' ",
        "  WHEN file_size BETWEEN 102400 AND 1048576 THEN '100KB-1MB' ",
        "  WHEN file_size BETWEEN 1048576 AND 10485760 THEN '1MB-10MB' ",
        "  WHEN file_size BETWEEN 10485760 AND 104857600 THEN '10MB-100MB' ",
        "  ELSE '> 100MB' ",
        "END as size_range, ",
        "COUNT(*) as file_count, ",
        "SUM(file_size) as total_size ",
        "FROM file_uploads ",
        "WHERE status = 1 ",
        "GROUP BY size_range ",
        "ORDER BY ",
        "  CASE size_range ",
        "    WHEN '< 1KB' THEN 1 ",
        "    WHEN '1KB-10KB' THEN 2 ",
        "    WHEN '10KB-100KB' THEN 3 ",
        "    WHEN '100KB-1MB' THEN 4 ",
        "    WHEN '1MB-10MB' THEN 5 ",
        "    WHEN '10MB-100MB' THEN 6 ",
        "    ELSE 7 ",
        "  END",
        "</script>"
    })
    java.util.List<java.util.Map<String, Object>> findFileUploadSizeDistribution();

    /**
     * 查询即将过期的文件上传记录
     *
     * @param days 天数
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads WHERE expire_at IS NOT NULL AND expire_at <= DATE_ADD(NOW(), INTERVAL #{days} DAY) AND status = 1 ORDER BY expire_at ASC LIMIT 100")
    List<FileUpload> findExpiringFiles(@Param("days") Integer days);

    /**
     * 查询已过期的文件上传记录
     *
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads WHERE expire_at IS NOT NULL AND expire_at < NOW() AND status = 1 ORDER BY expire_at ASC LIMIT 100")
    List<FileUpload> findExpiredFiles();

    /**
     * 查询需要清理的文件上传记录
     *
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads WHERE status = 0 ORDER BY updated_at ASC LIMIT 100")
    List<FileUpload> findFilesToClean();

    /**
     * 查询热门文件上传记录
     *
     * @param limit 限制数量
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads WHERE status = 1 ORDER BY download_count DESC, share_count DESC LIMIT #{limit}")
    List<FileUpload> findPopularFiles(@Param("limit") Integer limit);

    /**
     * 查询大文件上传记录
     *
     * @param size 大小（字节）
     * @param limit 限制数量
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads WHERE file_size >= #{size} AND status = 1 ORDER BY file_size DESC LIMIT #{limit}")
    List<FileUpload> findLargeFiles(@Param("size") Long size, @Param("limit") Integer limit);

    /**
     * 查询小文件上传记录
     *
     * @param size 大小（字节）
     * @param limit 限制数量
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads WHERE file_size <= #{size} AND status = 1 ORDER BY file_size ASC LIMIT #{limit}")
    List<FileUpload> findSmallFiles(@Param("size") Long size, @Param("limit") Integer limit);

    /**
     * 查询最近上传的文件上传记录
     *
     * @param limit 限制数量
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads WHERE status = 1 ORDER BY created_at DESC LIMIT #{limit}")
    List<FileUpload> findRecentFiles(@Param("limit") Integer limit);

    /**
     * 查询待审核的文件上传记录
     *
     * @param limit 限制数量
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads WHERE audit_status = 1 ORDER BY created_at ASC LIMIT #{limit}")
    List<FileUpload> findFilesToAudit(@Param("limit") Integer limit);

    /**
     * 查询已拒绝的文件上传记录
     *
     * @param limit 限制数量
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads WHERE audit_status = 3 ORDER BY audit_time DESC LIMIT #{limit}")
    List<FileUpload> findRejectedFiles(@Param("limit") Integer limit);

    /**
     * 查询已通过的文件上传记录
     *
     * @param limit 限制数量
     * @return 文件上传记录列表
     */
    @Select("SELECT * FROM file_uploads WHERE audit_status = 2 ORDER BY audit_time DESC LIMIT #{limit}")
    List<FileUpload> findApprovedFiles(@Param("limit") Integer limit);

    /**
     * 查询文件上传记录综合统计
     *
     * @return 文件上传记录综合统计
     */
    @Select("SELECT " +
            "COUNT(*) as total_files, " +
            "SUM(file_size) as total_size, " +
            "AVG(file_size) as avg_size, " +
            "MAX(file_size) as max_size, " +
            "MIN(file_size) as min_size, " +
            "COUNT(CASE WHEN status = 1 THEN 1 END) as active_files, " +
            "COUNT(CASE WHEN status = 0 THEN 1 END) as inactive_files, " +
            "COUNT(CASE WHEN audit_status = 1 THEN 1 END) as pending_files, " +
            "COUNT(CASE WHEN audit_status = 2 THEN 1 END) as approved_files, " +
            "COUNT(CASE WHEN audit_status = 3 THEN 1 END) as rejected_files, " +
            "COUNT(CASE WHEN expire_at IS NOT NULL AND expire_at < NOW() THEN 1 END) as expired_files, " +
            "COUNT(CASE WHEN expire_at IS NOT NULL AND expire_at <= DATE_ADD(NOW(), INTERVAL 7 DAY) THEN 1 END) as expiring_files, " +
            "COUNT(CASE WHEN download_count > 0 THEN 1 END) as downloaded_files, " +
            "COUNT(CASE WHEN share_count > 0 THEN 1 END) as shared_files, " +
            "COUNT(CASE WHEN backup_count > 0 THEN 1 END) as backed_up_files, " +
            "SUM(download_count) as total_downloads, " +
            "SUM(share_count) as total_shares, " +
            "SUM(backup_count) as total_backups " +
            "FROM file_uploads")
    java.util.Map<String, Object> findFileUploadComprehensiveStats();

    /**
     * 批量更新文件上传记录状态
     *
     * @param ids    文件上传记录ID列表
     * @param status 状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") java.util.List<Long> ids, @Param("status") Integer status);

    /**
     * 批量删除文件上传记录
     *
     * @param ids 文件上传记录ID列表
     * @return 影响行数
     */
    int batchDelete(@Param("ids") java.util.List<Long> ids);

    /**
     * 批量增加下载次数
     *
     * @param ids 文件上传记录ID列表
     * @return 影响行数
     */
    int batchIncreaseDownloadCount(@Param("ids") java.util.List<Long> ids);

    /**
     * 批量增加分享次数
     *
     * @param ids 文件上传记录ID列表
     * @return 影响行数
     */
    int batchIncreaseShareCount(@Param("ids") java.util.List<Long> ids);

    /**
     * 批量增加备份次数
     *
     * @param ids 文件上传记录ID列表
     * @return 影响行数
     */
    int batchIncreaseBackupCount(@Param("ids") java.util.List<Long> ids);

    /**
     * 批量更新文件上传记录审核状态
     *
     * @param ids         文件上传记录ID列表
     * @param auditStatus 审核状态
     * @param auditorId   审核人ID
     * @param auditorName 审核人姓名
     * @param auditTime   审核时间
     * @param auditOpinion 审核意见
     * @return 影响行数
     */
    int batchUpdateAuditStatus(
            @Param("ids") java.util.List<Long> ids,
            @Param("auditStatus") Integer auditStatus,
            @Param("auditorId") Long auditorId,
            @Param("auditorName") String auditorName,
            @Param("auditTime") java.time.LocalDateTime auditTime,
            @Param("auditOpinion") String auditOpinion);

    /**
     * 批量更新文件上传记录过期时间
     *
     * @param ids       文件上传记录ID列表
     * @param expireAt  过期时间
     * @return 影响行数
     */
    int batchUpdateExpireTime(@Param("ids") java.util.List<Long> ids, @Param("expireAt") java.time.LocalDateTime expireAt);

    /**
     * 批量更新文件上传记录分享过期时间
     *
     * @param ids           文件上传记录ID列表
     * @param shareExpireAt 分享过期时间
     * @return 影响行数
     */
    int batchUpdateShareExpireTime(@Param("ids") java.util.List<Long> ids, @Param("shareExpireAt") java.time.LocalDateTime shareExpireAt);

    /**
     * 批量更新文件上传记录备份过期时间
     *
     * @param ids             文件上传记录ID列表
     * @param backupExpireAt 备份过期时间
     * @return 影响行数
     */
    int batchUpdateBackupExpireTime(@Param("ids") java.util.List<Long> ids, @Param("backupExpireAt") java.time.LocalDateTime backupExpireAt);
}