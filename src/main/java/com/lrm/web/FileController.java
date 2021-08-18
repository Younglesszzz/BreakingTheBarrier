package com.lrm.web;

import com.lrm.exception.NotFoundException;
import com.lrm.service.FileService;
import com.lrm.util.FileUtils;
import com.lrm.util.TokenInfo;
import com.lrm.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/files")
public class FileController {
    private final static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.upload-path}")
    private String uploadFilePath;

    @Value("${file.download-path}")
    private String downloadFilePath;

    @Autowired
    private FileService fileService;

    @GetMapping
    public String index() {
        return "index";
    }

    /**
     *
     * @param query 用户想要查询的文件关键词
     * @return 查询结果
     */
    @GetMapping(value = "/find")
    @ResponseBody
    public Result find(@RequestParam("query") String query) {
        Page<com.lrm.po.File> filePage = fileService.findFile(query);

        HashMap<String, Object> hashMap = new HashMap<>(2);

        hashMap.put("page", filePage);
        return new Result(hashMap, "文件查询成功");
    }

    /**
     *
     * @param uploadFile 上传文件
     * @param fileTagName 前端传入对应标签名字
     */
    @PostMapping(value = "/upload")
    @ResponseBody
    public Result upload(@RequestParam("uploadFile") MultipartFile uploadFile, @RequestParam("fileTagName") String fileTagName, HttpServletRequest request) throws IOException {
        //打印上传文件信息
        logFileInfo(uploadFile, uploadFilePath);

        //多媒体文件转为po file
        com.lrm.po.File newFile = FileUtils.convertFile(uploadFile);

        //判断文件所在目录是否存在，不存在就创建对应的目录
        File dest = FileUtils.buildDest(uploadFilePath + uploadFile.getOriginalFilename());
        uploadFile.transferTo(dest);

        //获取用户
        Long userId = TokenInfo.getCustomUserId(request);

        //调用service 服务，储存到数据库，进行上传相关逻辑的处理
        fileService.saveFile(newFile, fileTagName, dest.getAbsolutePath(), userId);

        Map<String, String> hashMap = new HashMap<>(16);
        hashMap.put("contentType", uploadFile.getContentType());
        hashMap.put("fileName", uploadFile.getOriginalFilename());
        //这么写有缺陷，会丢失精度，应该四舍五入使用Decimal类（小问题，待更改-------------
        hashMap.put("fileSize", String.valueOf(uploadFile.getSize() / 1024) + "KB");//单位是B,大写B代表byte，小写b代表bit

        Result res = new Result(hashMap, "上传成功");
        return res;
    }

    /**
     * 有待优化，感觉应该用{id}这样，直接get，不用文件名
     * @param fileName 文件名
     * @param response 响应，下载真正的文件
     * @param request  请求，获取UserId
     */
    @GetMapping("/download")
    @ResponseBody
    public Result fileDownload(@RequestParam("fileName") String fileName, HttpServletResponse response, HttpServletRequest request) throws IOException {
        File file = new File(downloadFilePath + fileName);//如何解决中文问题
        if (!file.exists()) {
            throw new NotFoundException("下载文件不存在");
        }

        Long userId = TokenInfo.getCustomUserId(request);

        fileService.downloadFile(fileName, userId);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", file.getName());

        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int)file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] buff = new byte[1024];
        OutputStream os = response.getOutputStream();
        int i = 0;
        while ((i = bis.read(buff)) != -1) {
            os.write(buff, 0, i);
            os.flush();
        }
        return new Result(hashMap, "下载成功");
    }

    private void logFileInfo(MultipartFile file, String uploadFilePath) {
        logger.info("文件名：" + file.getName());
        logger.info("原文件名：" + file.getOriginalFilename());
        logger.info("文件类型" + file.getContentType());
        logger.info("文件地址: " + (uploadFilePath + file.getOriginalFilename()));
    }

}
