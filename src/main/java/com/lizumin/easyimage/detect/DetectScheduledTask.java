package com.lizumin.easyimage.detect;

import com.lizumin.easyimage.Dao.ImageRepository;
import com.lizumin.easyimage.Dao.ImageTagRepository;
import com.lizumin.easyimage.constant.enums.ImageState;
import com.lizumin.easyimage.controller.LibraryController;
import com.lizumin.easyimage.model.entity.LabelImage;
import com.lizumin.easyimage.model.entity.Tag;
import com.lizumin.easyimage.utils.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
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

    @Scheduled(fixedDelay = 10000)
    public void detectImages(){
        List<LabelImage> labelImageList = this.imageRepository.findLabelImagesByState(ImageState.WAITING);

        if (labelImageList.size() == 0){
            return;
        }

        labelImageList.forEach(( LabelImage image)->{
            String imagePath = PathUtil.imagesStorePath() + image.getFilePath();

            //make sure the file exists
            File file = new File(imagePath);
            if (!file.exists()){
                return;
            }

            this.logger.info(image.getFilePath() + " is detecting");

            List<String> tags = Detect.detect(PathUtil.imagesStorePath() + image.getFilePath());

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
}
