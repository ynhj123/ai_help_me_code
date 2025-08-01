package com.example.ecommerce.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 文件上传DTO类
 */
@Data
public class FileUploadDTO {

    /**
     * 原始文件名
     */
    @NotBlank(message = "原始文件名不能为空")
    @Size(max = 255, message = "原始文件名长度不能超过255个字符")
    private String originalName;

    /**
     * 存储文件名
     */
    @Size(max = 255, message = "存储文件名长度不能超过255个字符")
    private String storedName;

    /**
     * 文件路径
     */
    @Size(max = 500, message = "文件路径长度不能超过500个字符")
    private String filePath;

    /**
     * 文件大小（字节）
     */
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;

    /**
     * 文件类型（MIME类型）
     */
    @NotBlank(message = "文件类型不能为空")
    @Size(max = 100, message = "文件类型长度不能超过100个字符")
    private String mimeType;

    /**
     * 文件分类
     */
    @NotBlank(message = "文件分类不能为空")
    @Size(max = 50, message = "文件分类长度不能超过50个字符")
    private String fileType;

    /**
     * 文件访问URL
     */
    @Size(max = 500, message = "文件访问URL长度不能超过500个字符")
    private String fileUrl;

    /**
     * 文件哈希值
     */
    @Size(max = 64, message = "文件哈希值长度不能超过64个字符")
    private String fileHash;

    /**
     * 文件状态：1正常，0删除
     */
    private Integer status = 1;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 创建人姓名
     */
    @Size(max = 100, message = "创建人姓名长度不能超过100个字符")
    private String createdByName;

    /**
     * 文件描述
     */
    @Size(max = 500, message = "文件描述长度不能超过500个字符")
    private String description;

    /**
     * 文件标签
     */
    @Size(max = 500, message = "文件标签长度不能超过500个字符")
    private String tags;

    /**
     * 文件下载次数
     */
    private Integer downloadCount = 0;

    /**
     * 文件预览URL
     */
    @Size(max = 500, message = "文件预览URL长度不能超过500个字符")
    private String previewUrl;

    /**
     * 文件缩略图URL
     */
    @Size(max = 500, message = "文件缩略图URL长度不能超过500个字符")
    private String thumbnailUrl;

    /**
     * 文件宽度（图片）
     */
    private Integer width;

    /**
     * 文件高度（图片）
     */
    private Integer height;

    /**
     * 文件时长（音视频）
     */
    private Integer duration;

    /**
     * 文件比特率（音视频）
     */
    private Integer bitrate;

    /**
     * 文件格式
     */
    @Size(max = 50, message = "文件格式长度不能超过50个字符")
    private String format;

    /**
     * 文件版本
     */
    @Size(max = 50, message = "文件版本长度不能超过50个字符")
    private String version;

    /**
     * 文件权限：1公开，2私有，3指定用户
     */
    private Integer permission = 1;

    /**
     * 有效期（天）
     */
    private Integer expireDays;

    /**
     * 过期时间
     */
    private LocalDateTime expireAt;

    /**
     * 是否允许下载：1允许，0不允许
     */
    private Integer allowDownload = 1;

    /**
     * 是否允许预览：1允许，0不允许
     */
    private Integer allowPreview = 1;

    /**
     * 是否允许分享：1允许，0不允许
     */
    private Integer allowShare = 1;

    /**
     * 分享密码
     */
    @Size(max = 100, message = "分享密码长度不能超过100个字符")
    private String sharePassword;

    /**
     * 分享次数限制
     */
    private Integer shareLimit;

    /**
     * 分享次数
     */
    private Integer shareCount = 0;

    /**
     * 分享过期时间
     */
    private LocalDateTime shareExpireAt;

    /**
     * IP地址
     */
    @Size(max = 45, message = "IP地址长度不能超过45个字符")
    private String ipAddress;

    /**
     * 用户代理
     */
    @Size(max = 1000, message = "用户代理长度不能超过1000个字符")
    private String userAgent;

    /**
     * 上传来源：1网页，2API，3移动端，4其他
     */
    private Integer uploadSource = 1;

    /**
     * 存储位置：1本地，2云存储，3CDN
     */
    private Integer storageLocation = 1;

    /**
     * 存储桶（云存储）
     */
    @Size(max = 100, message = "存储桶长度不能超过100个字符")
    private String storageBucket;

    /**
     * 存储区域（云存储）
     */
    @Size(max = 50, message = "存储区域长度不能超过50个字符")
    private String storageRegion;

    /**
     * 存储路径（云存储）
     */
    @Size(max = 500, message = "存储路径长度不能超过500个字符")
    private String storagePath;

    /**
     * 访问密钥（云存储）
     */
    @Size(max = 100, message = "访问密钥长度不能超过100个字符")
    private String accessKey;

    /**
     * 私有密钥（云存储）
     */
    @Size(max = 100, message = "私有密钥长度不能超过100个字符")
    private String secretKey;

    /**
     * 文件加密：1加密，2未加密
     */
    private Integer encrypted = 2;

    /**
     * 加密算法
     */
    @Size(max = 50, message = "加密算法长度不能超过50个字符")
    private String encryptionAlgorithm;

    /**
     * 加密密钥
     */
    @Size(max = 100, message = "加密密钥长度不能超过100个字符")
    private String encryptionKey;

    /**
     * 加密IV
     */
    @Size(max = 100, message = "加密IV长度不能超过100个字符")
    private String encryptionIv;

    /**
     * 文件校验和
     */
    @Size(max = 100, message = "文件校验和长度不能超过100个字符")
    private String checksum;

    /**
     * 文件校验算法
     */
    @Size(max = 50, message = "文件校验算法长度不能超过50个字符")
    private String checksumAlgorithm;

    /**
     * 文件备份：1已备份，2未备份
     */
    private Integer backup = 2;

    /**
     * 备份路径
     */
    @Size(max = 500, message = "备份路径长度不能超过500个字符")
    private String backupPath;

    /**
     * 备份次数
     */
    private Integer backupCount = 0;

    /**
     * 备份过期时间
     */
    private LocalDateTime backupExpireAt;

    /**
     * 文件压缩：1已压缩，2未压缩
     */
    private Integer compressed = 2;

    /**
     * 压缩算法
     */
    @Size(max = 50, message = "压缩算法长度不能超过50个字符")
    private String compressionAlgorithm;

    /**
     * 压缩率
     */
    private Double compressionRatio;

    /**
     * 原始文件大小（压缩前）
     */
    private Long originalFileSize;

    /**
     * 文件版本号
     */
    private Integer versionNumber = 1;

    /**
     * 文件父ID
     */
    private Long parentId;

    /**
     * 文件子文件数量
     */
    private Integer childCount = 0;

    /**
     * 文件层级
     */
    private Integer level = 1;

    /**
     * 文件排序
     */
    private Integer sortOrder = 0;

    /**
     * 文件路径层级
     */
    @Size(max = 1000, message = "文件路径层级长度不能超过1000个字符")
    private String path;

    /**
     * 文件完整路径
     */
    @Size(max = 1000, message = "文件完整路径长度不能超过1000个字符")
    private String fullPath;

    /**
     * 文件扩展名
     */
    @Size(max = 20, message = "文件扩展名长度不能超过20个字符")
    private String extension;

    /**
     * 文件图标
     */
    @Size(max = 100, message = "文件图标长度不能超过100个字符")
    private String icon;

    /**
     * 文件颜色
     */
    @Size(max = 20, message = "文件颜色长度不能超过20个字符")
    private String color;

    /**
     * 文件标签颜色
     */
    @Size(max = 20, message = "文件标签颜色长度不能超过20个字符")
    private String tagColor;

    /**
     * 文件备注
     */
    @Size(max = 500, message = "文件备注长度不能超过500个字符")
    private String remarks;

    /**
     * 文件审核状态：1待审核，2已通过，3已拒绝
     */
    private Integer auditStatus = 1;

    /**
     * 审核人ID
     */
    private Long auditorId;

    /**
     * 审核人姓名
     */
    @Size(max = 100, message = "审核人姓名长度不能超过100个字符")
    private String auditorName;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核意见
     */
    @Size(max = 500, message = "审核意见长度不能超过500个字符")
    private String auditOpinion;

    /**
     * 文件创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 文件更新时间
     */
    private LocalDateTime updatedAt;
}