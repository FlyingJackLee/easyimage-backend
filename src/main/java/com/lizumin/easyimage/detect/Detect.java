package com.lizumin.easyimage.detect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/26 12:46 am
 * 4
 */
public class Detect {
    private final static Logger logger = LoggerFactory.getLogger(DetectScheduledTask.class);

    final static String[] labels = new String[]{
            "person", "bicycle", "car", "motorcycle", "airplane", "bus", "train", "truck", "boat", "traffic light", "fire hydrant", "stop sign", "parking meter", "bench", "bird", "cat", "dog", "horse", "sheep", "cow", "elephant", "bear", "zebra", "giraffe", "backpack", "umbrella", "handbag", "tie", "suitcase", "frisbee", "skis", "snowboard", "sports ball", "kite", "baseball bat", "baseball glove", "skateboard", "surfboard", "tennis racket", "bottle", "wine glass", "cup", "fork", "knife", "spoon", "bowl", "banana", "apple", "sandwich", "orange", "broccoli", "carrot", "hot dog", "pizza", "donut", "cake", "chair", "couch", "potted plant", "bed", "dining table", "toilet", "tv", "laptop", "mouse", "remote", "keyboard", "cell phone", "microwave", "oven", "toaster", "sink", "refrigerator", "book", "clock", "vase", "scissors", "teddy bear", "hair drier", "toothbrush"
    };
    final static String yoloEnv = "/Users/jack/OneDrive/UofGlasgow_Files/project/code/yolov3";
    final static String pythonEnv = "/Users/jack/opt/anaconda3/envs/yolo3/bin/python";

    public static List<String> detect(String imagePath) {
        return detect(yoloEnv,pythonEnv,imagePath);
    }

    public static List<String> detect(String yoloEnv,String pythonEnv,String imagePath){
        String command = pythonEnv + " detect.py --source " + imagePath ;

        String[] result = null;

        try {
            Process process = Runtime.getRuntime().exec(
                    command,null, new File(yoloEnv)
            );

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));

            String output = null;

            logger.debug("Here is the standard output of the command:\n");

            while ((output = stdInput.readLine()) != null) {
                if (output.contains(imagePath) && output.contains("448x640")){
                    result = output.split("448x640")[1].split("\\(")[0].split(",");
                    logger.debug(output);
                }
            }

            // read any errors from the attempted command
            logger.debug("Here is the standard error of the command (if any):\n");
            while ((output = stdError.readLine()) != null) {
                logger.debug(output);
            }

        }catch (IOException  e){
            e.printStackTrace();
        }
        List<String> trimResult = new ArrayList<>();
        if (result != null && result.length > 1){
            for (int i = 0; i < result.length - 1; i++) {
                trimResult.add(result[i].trim());
            }
        }

        return trimResult;
    }
}
