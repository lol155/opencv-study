import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.*;

public class FirstOpenCV {
    static {  
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //注意程序运行的时候需要在VM option添加该行 指明opencv的dll文件所在路径  
        //-Djava.library.path=$PROJECT_DIR$\opencv\x64  
    }

    public static void main(String[] args) {
        test1();
//        test2();
//        test3();
    }

    private static void test1() {
        Mat mat = Imgcodecs.imread("20170923154329.png");

        mat = mat.colRange(new Range(mat.cols() * 3 / 10, mat.cols()));

        int changeCount = 0 ;
        int num_rows = mat.rows();
        int num_col = mat.cols();
        RangePosition rangePosition = new RangePosition();
        List<Mat> matList = new ArrayList<Mat>();
        for (int rowNum = 0; rowNum < num_rows; rowNum++) {
            boolean isInARange = false;
            inner:for (int colNum = 0; colNum < num_col; colNum++) {
                // 获取每个像素
                double[] clone = mat.get(rowNum, colNum).clone();
                if (isStarColor(clone)) {
                    rangePosition.compareStartAndSet(RangePosition.PositionType.START_X,colNum);
                    rangePosition.compareStartAndSet(RangePosition.PositionType.END_X,colNum);
                    rangePosition.compareStartAndSet(RangePosition.PositionType.START_Y,rowNum);
                    rangePosition.compareStartAndSet(RangePosition.PositionType.END_Y,rowNum);

                    // 红色范围,全部设置为黑色,
                    clone[0] = 0;
                    clone[1] = 0;
                    clone[2] = 0;
                    mat.put(rowNum, colNum, clone);
                    changeCount++;
                    continue inner;
                }
                clone[0] = 255;
                clone[1] = 255;
                clone[2] = 255;
                mat.put(rowNum, colNum, clone);
            }
        }
        Imgcodecs.imwrite("000001.png", mat);
        System.out.println(changeCount);
    }

    private static boolean isStarColor(double[] clone) {
        return inRange(1, clone[0]) && inRange(80, clone[1]) && inRange(255, clone[2]);
    }

    private static boolean inRange(double val, double x) {
        double offset = 30;
        return x >= val - offset && x <= val + offset;

    }
}