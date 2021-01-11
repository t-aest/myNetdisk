package com.taest.mynetdisk.file.service.impl;

import com.taest.mynetdisk.file.entity.File;
import com.taest.mynetdisk.file.mapper.FileMapper;
import com.taest.mynetdisk.file.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-01-11
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

}
