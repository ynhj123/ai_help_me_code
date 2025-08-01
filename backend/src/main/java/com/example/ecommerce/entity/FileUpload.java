package com.example.ecommerce.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 文件上传实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileUpload extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 存储文件名
     */
    private String storedName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型（MIME类型）
     */
    private String mimeType;

    /**
     * 文件分类
     */
    private String fileType;

    /**
     * 文件访问URL
     */
    private String fileUrl;

    /**
     * 文件哈希值
     */
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
    private String createdByName;

    /**
     * 文件描述
     */
    private String description;

    /**
     * 文件标签
     */
    private String tags;

    /**
     * 文件下载次数
     */
    private Integer downloadCount = 0;

    /**
     * 文件预览URL
     */
    private String previewUrl;

    /**
     * 文件缩略图URL
     */
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
    private String format;

    /**
     * 文件版本
     */
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
    private String ipAddress;

    /**
     * 用户代理
     */
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
    private String storageBucket;

    /**
     * 存储区域（云存储）
     */
    private String storageRegion;

    /**
     * 存储路径（云存储）
     */
    private String storagePath;

    /**
     * 访问密钥（云存储）
     */
    private String accessKey;

    /**
     * 私有密钥（云存储）
     */
    private String secretKey;

    /**
     * 文件加密：1加密，2未加密
     */
    private Integer encrypted = 2;

    /**
     * 加密算法
     */
    private String encryptionAlgorithm;

    /**
     * 加密密钥
     */
    private String encryptionKey;

    /**
     * 加密IV
     */
    private String encryptionIv;

    /**
     * 文件校验和
     */
    private String checksum;

    /**
     * 文件校验算法
     */
    private String checksumAlgorithm;

    /**
     * 文件备份：1已备份，2未备份
     */
    private Integer backup = 2;

    /**
     * 备份路径
     */
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
    private String path;

    /**
     * 文件完整路径
     */
    private String fullPath;

    /**
     * 文件扩展名
     */
    private String extension;

    /**
     * 文件图标
     */
    private String icon;

    /**
     * 文件颜色
     */
    private String color;

    /**
     * 文件标签颜色
     */
    private String tagColor;

    /**
     * 文件备注
     */
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
    private String auditorName;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核意见
     */
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