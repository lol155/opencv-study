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
    private static int count = 0;
    public static void main(String[] args) {
//        Mat mat2 = Imgcodecs.imread("20170923154329.png");
//        mat2 = mat2.colRange(new Range(mat2.cols() * 3 / 10, mat2.cols()));
//        Mat mat = Imgcodecs.imread("00.png");
//        System.out.println(isFiveFlag(mat,mat2));


        boolean b = test1();
        System.out.println(b);
    }

    private static boolean test1() {
        Mat mat = Imgcodecs.imread("20170923154329.png");
        mat = mat.colRange(new Range(mat.cols() * 3 / 10, mat.cols()));
//        int changeCount = 0 ;
        int num_rows = mat.rows();
        int num_col = mat.cols();
        RangePosition rangePosition = new RangePosition();
//        List<Mat> matList = new ArrayList<Mat>();
        boolean startedCollectRange = false;
        boolean isRangeEnd = false;
        for (int rowNum = 0; rowNum < num_rows; rowNum++) {
            boolean rowIsInARange = false;
            isRangeEnd = false;
            for (int colNum = 0; colNum < num_col; colNum++) {
                // 获取每个像素
                double[] clone = mat.get(rowNum, colNum).clone();
                if (isStarColor(clone)) {
                    rowIsInARange = true;
                    startedCollectRange = true;
                    rangePosition.compareStartAndSet(RangePosition.PositionType.START_ROW,rowNum);
                    rangePosition.compareStartAndSet(RangePosition.PositionType.END_ROW,rowNum);
                    rangePosition.compareStartAndSet(RangePosition.PositionType.START_COL,colNum);
                    rangePosition.compareStartAndSet(RangePosition.PositionType.END_COL,colNum);

                    // 星星颜色,全部设置为黑色,
                    clone[0] = 0;
                    clone[1] = 0;
                    clone[2] = 0;
                    mat.put(rowNum, colNum, clone);
//                    changeCount++;
                    continue;
                }
                //除星星外的其他颜色全部置为白色
                clone[0] = 255;
                clone[1] = 255;
                clone[2] = 255;
                mat.put(rowNum, colNum, clone);
            }
            if(!rowIsInARange && startedCollectRange){
                isRangeEnd = true;
            }

            if (isRangeEnd) {
                if (isFiveFlag(rangePosition.cutRange(mat),mat)) {
                    return true;
                }
//                matList.add(rangePosition.cutRange(mat));
                rangePosition = new RangePosition();
                startedCollectRange = false;
            }
        }
//        for (int i = 0; i < matList.size(); i++) {
//            Imgcodecs.imwrite("0"+i+".png", matList.get(i));
//        }
//        Imgcodecs.imwrite("000001.png", mat);
//        System.out.println(changeCount);
        return false;
    }

    private static boolean isFiveFlag(Mat matRange, Mat mat) {
        int blackCount = 0 ;
        //判断宽度是否大于一半
        if(matRange.cols() < mat.cols() / 2 ){
            return false;
        }
        boolean startedCollectRange = false;
        boolean isRangeEnd = false;
        RangePosition rangePosition = new RangePosition();
        List<Mat> starMatList = new ArrayList<Mat>();
        for (int colNum = 0; colNum < matRange.cols(); colNum++) {
            boolean colIsInARange = false;
            isRangeEnd = false;
            for (int rowNum = 0; rowNum < matRange.rows(); rowNum++) {
                double[] clone = matRange.get(rowNum, colNum).clone();
                if(clone[0] == 0){
                    colIsInARange = true;
                    startedCollectRange = true;
                    rangePosition.compareStartAndSet(RangePosition.PositionType.START_ROW,rowNum);
                    rangePosition.compareStartAndSet(RangePosition.PositionType.END_ROW,rowNum);
                    rangePosition.compareStartAndSet(RangePosition.PositionType.START_COL,colNum);
                    rangePosition.compareStartAndSet(RangePosition.PositionType.END_COL,colNum);
                    blackCount ++ ;
                }
            }
            if(!colIsInARange && startedCollectRange || colNum == matRange.cols() - 1 ){
                isRangeEnd = true;
            }
            if (isRangeEnd) {
                starMatList.add(rangePosition.cutRange(matRange));
                rangePosition = new RangePosition();
                startedCollectRange = false;
            }
        }
//        System.out.println("starMatList: " + starMatList.size());
//        System.out.println("blackCount: " + blackCount);
        if (starMatList.size() < 5) {
            return false;
        }

        for (Mat starMat : starMatList) {
            if(!isMoreThan(starMat,50)){
                return false;
            }
        }
        return true;
    }

    /**
     * 黑色占比是否达到
     * @param starMat 星星图
     * @param ratio 比例 百分之X  如:50 则代表 占比百分之五十
     * @return
     */
    private static boolean isMoreThan(Mat starMat, int ratio) {
        int starColorCount = 0 ;
        int totalCount = starMat.cols() * starMat.rows();
        for (int rowNum = 0; rowNum < starMat.rows(); rowNum++) {
            for (int colNum = 0; colNum < starMat.cols(); colNum++) {
                if(starMat.get(rowNum,colNum)[0] == 0){
                    starColorCount ++ ;
                }
            }
        }
        Imgcodecs.imwrite("1"+ count++ +".png",starMat);
//        System.out.println("starColorCount:" +starColorCount + " totalCount:" + totalCount + " ratio:" +ratio );
//        System.out.println(0.0 * starColorCount * 100 / (totalCount * ratio));
        return starColorCount * 100 >= totalCount * ratio;
    }


    private static boolean isStarColor(double[] clone) {
        return inRange(1, clone[0]) && inRange(80, clone[1]) && inRange(255, clone[2]);
    }

    private static boolean inRange(double checkVal, double desVal) {
        double offset = 30;
        return desVal >= checkVal - offset && desVal <= checkVal + offset;
    }
}