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

import java.io.File;
import java.util.List;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/26 1:41 am
 * 4
 */
@Component
public class DetectScheduledTask {
    private final Logger logger = LoggerFactory.getLogger(DetectScheduledTask.class);

    private ImageRepository imageRepository;
    private ImageTagRepository tagRepository;

    private Environment env;

    private Detect detect;

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

            List<String> tags = this.detect.detect(this.env.getProperty("image.save.path") + image.getFilePath());

            tags.forEach((String label)->{
                Tag tag = new Tag(label);
                image.getTags().add(tag);
                this.tagRepository.save(tag);
            });

            image.setState(ImageState.DONE);
            this.imageRepository.save(image);

            this.logger.info(image.getFilePath() + " detect successfully!");
        });
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
    public void setDetect(Detect detect) {
        this.detect = detect;
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }
}
