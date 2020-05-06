package com.wrz.crud.controller.File;

import com.wrz.crud.utils.random.RandomSelf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author wrz
 * @data 2020/4/26-21:03
 */
@Controller
@RequestMapping("/view")
public class FileController {

    @Autowired
    RandomSelf randomSelf;


    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return "false";
        }
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);

        String path = "D:/1fileupload/file" ;

        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowTime = df.format(day);

        File dest = new File(path + "/" + nowTime +  randomSelf.getCode(12));
        System.out.println(dest);

        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            System.out.println("上传成功");
            return "true";
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            e.printStackTrace();
            return "false";
        }
    }


    /**
     * 实现多文件上传
     *
     * */
    //public @ResponseBody String multifileUpload(@RequestParam("fileName")List<MultipartFile> files)

    @RequestMapping(value="multifileUpload",method= RequestMethod.POST)
    @ResponseBody
    public String multifileUpload(HttpServletRequest request){

        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("fileName");

        if(files.isEmpty()){
            return "false";
        }

        String path = "D:/1fileupload/file" ;

        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowTime = df.format(day);

        for(MultipartFile file:files){
            String fileName = file.getOriginalFilename();
            int size = (int) file.getSize();

            if(file.isEmpty()){
                return "false";
            }else{
                File dest = new File(path + "/"+ nowTime + fileName);
                if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                try {
                    file.transferTo(dest);
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "false";
                }
            }
        }
        return "view/fileUpload";
    }

    @RequestMapping("/download")
    public String downLoad(HttpServletResponse response,@RequestParam("fileName") String fileName) throws UnsupportedEncodingException {
        String filename= fileName;
        String filePath = "D:/1fileupload" ;
        File file = new File(filePath + "/" + filename);
        if(file.exists()){ //判断文件父目录是否存在
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            // response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode(filename,"UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download---" + filename);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
}
