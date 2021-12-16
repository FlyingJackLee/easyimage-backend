package com.lizumin.easyimage.detect;

import com.lizumin.easyimage.Dao.ImageRepository;
import com.lizumin.easyimage.Dao.ImageTagRepository;
import com.lizumin.easyimage.constant.enums.ImageState;
import com.lizumin.easyimage.model.entity.LabelImage;
import com.lizumin.easyimage.model.entity.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/26 1:41 am
 * 4
 */
@Component
public class DetectScheduledTask {
    private final Logger logger = LoggerFactory.getLogger(DetectScheduledTask.class);

    final String[] labels = new String[]{
            "person", "bicycle", "car", "motorcycle", "airplane", "bus", "train", "truck", "boat", "traffic light", "fire hydrant", "stop sign", "parking meter", "bench", "bird", "cat", "dog", "horse", "sheep", "cow", "elephant", "bear", "zebra", "giraffe", "backpack", "umbrella", "handbag", "tie", "suitcase", "frisbee", "skis", "snowboard", "sports ball", "kite", "baseball bat", "baseball glove", "skateboard", "surfboard", "tennis racket", "bottle", "wine glass", "cup", "fork", "knife", "spoon", "bowl", "banana", "apple", "sandwich", "orange", "broccoli", "carrot", "hot dog", "pizza", "donut", "cake", "chair", "couch", "potted plant", "bed", "dining table", "toilet", "tv", "laptop", "mouse", "remote", "keyboard", "cell phone", "microwave", "oven", "toaster", "sink", "refrigerator", "book", "clock", "vase", "scissors", "teddy bear", "hair drier", "toothbrush"
    };
    private String yoloEnv ;
    private String pythonEnv;


    private ImageRepository imageRepository;
    private ImageTagRepository tagRepository;
    private Environment env;


    public DetectScheduledTask(@Autowired Environment env){
        this.yoloEnv = env.getProperty("detect.yoloEnv");
        this.pythonEnv = env.getProperty("detect.pythonEnv");
    }

    @Scheduled(fixedDelay = 10000)
    public void detectImages(){

        List<LabelImage> labelImageList = this.imageRepository.findLabelImagesByState(ImageState.WAITING);

        if (labelImageList.size() == 0){
            return;
        }

        labelImageList.forEach(( LabelImage image)->{
            String imagePath = this.env.getProperty("image.save.path")  + image.getFilePath();

            //make sure the file exists
            File file = new File(imagePath);
            if (!file.exists()){
                return;
            }

            this.logger.info(image.getFilePath() + " is detecting");

            //1. create YOLOv3 Script
            String command = pythonEnv + " detect.py --source " + imagePath + " --nosave" ;
            List<String> tags = new ArrayList<>();

            try {
                Process process = Runtime.getRuntime().exec(
                        command,null, new File(yoloEnv)
                );

                try {
                    //waiting for it completes
                    process.waitFor();
                }
                catch (InterruptedException e){
                    logger.info("YOLOv3 script error.");
                }

                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(process.getInputStream()));

                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(process.getErrorStream()));

                String output = null;

                logger.debug("Here is the standard output of the command:\n");

                while ((output = stdInput.readLine()) != null) {
                    tags = checkLabel(output);
                    if (tags.size() > 0){
                        break;
                    }
//                if (output.contains(imagePath) && output.contains("448x640")){
//                    result = output.split("448x640")[1].split("\\(")[0].split(",");
//                    logger.info(output);
//                }
                }

                // read any errors from the attempted command
                if (tags.size() == 0){
                    logger.debug("Here is the standard error of the command (if any):\n");

                    while ((output = stdError.readLine()) != null) {
                        tags = checkLabel(output);
                        if (tags.size() > 0){
                            break;
                        }
//                if (output.contains(imagePath) && output.contains("448x640")){
//                    result = output.split("448x640")[1].split("\\(")[0].split(",");
//                    logger.debug(output);
//                }
                    }
                }

            }catch (IOException e){
                e.printStackTrace();
            }
            this.logger.info(image.getFilePath() + tags.size() + " tags found!");

            if (tags.size() > 0){
                tags.forEach((String label)->{
                    Tag tag = new Tag(label);
                    image.getTags().add(tag);
                    this.tagRepository.save(tag);
                });
            }

            image.setState(ImageState.DONE);
            this.imageRepository.save(image);

            this.logger.info(image.getFilePath() + " detect successfully!");
        });
    }

    private  List<String> checkLabel(String line){
        logger.debug("checking output " + line);

        List<String> results = new ArrayList<>();
        if ("".equals(line)){
            return results;
        }

        for (String label: labels) {
            logger.debug("checking label " + label);
            if (line.contains(label)){
                results.add(label);
            }
        }
        return results;
    }

    @Autowired
    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Autowired
    public void setTagRepository(ImageTagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }
}
