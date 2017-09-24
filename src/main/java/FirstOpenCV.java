import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Range;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

        Mat resultMat = mat.colRange(new Range(mat.cols() * 3 / 10, mat.cols()));
        Imgcodecs.imwrite("70.png", resultMat);
/*

        int changeCount = 0 ;
//        Mat mat = Imgcodecs.imread("star.png");
        int num_rows = mat.rows();
        int num_col = mat.cols();
//        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HSV);
        for (int i = 0; i < num_rows; i++) {
            inner:for (int j = 0; j < num_col; j++) {
                // 获取每个像素
                double[] clone = mat.get(i, j).clone();

                if (inRange(1, clone[0])) {
                    System.out.println(clone[1] + ":" + clone[2]);
                    if (inRange(80,clone[1])) {
                        if (inRange(255,clone[2])) {
                            // 红色范围,全部设置为黑色,
                            clone[0] = 0;
                            clone[1] = 0;
                            clone[2] = 0;
                            mat.put(i, j, clone);
                            changeCount++;
                            continue inner;
                        }
                    }
                }
                clone[0] = 255;
                clone[1] = 255;
                clone[2] = 255;
                mat.put(i, j, clone);
            }
        }
        Imgcodecs.imwrite("20170923154321.png", mat);
        System.out.println(mat);
        System.out.println(changeCount);*/
    }

    public static void test2() {
//        Mat mat = Imgcodecs.imread("20170923154329.png");
        Mat mat = Imgcodecs.imread("star.png");
        System.out.println("size:" + mat.size());
        Map<String, Integer> countMap = new HashMap<String, Integer>();

        int rows = mat.rows();
        int cols = mat.cols();
        for (int i = 0; i < rows; i++) {
            inner:for (int j = 0; j < cols; j++) {
                double[] clone = mat.get(i, j).clone();
                String key = clone[0] + "," + clone[1] + "," + clone[2];
                countMap.put(key, countMap.get(key) == null ? 0 : countMap.get(key) + 1);
//                System.out.println(key);
                /*if (inRange(255,clone[0])) {
                    if(inRange(80,clone[2])){
                        System.out.println(clone[1] + ":" + clone[2]);
                        System.out.println("333");
                        // 红色范围,全部设置为黑色,
                        clone[0] = 0;
                        clone[1] = 0;
                        clone[2] = 0;
                        mat.put(i, j, clone);
                        continue inner;
                    }
                }
                clone[0] = 255;
                clone[1] = 255;
                clone[2] = 255;
                mat.put(i, j, clone);*/
            }
        }
//        Imgcodecs.imwrite("20170923154321.png", mat);
        Set<Map.Entry<String, Integer>> entries = countMap.entrySet();
        Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            System.out.println(next.getKey() + ":" + next.getValue());
        }
            
        System.out.println(mat);
    }

    public  static  void test3() {
        Mat mat = Imgcodecs.imread("20170923154329.png");
        double[] clone = mat.get(144, 93);
        System.out.println(clone[0] + "," + clone[1] + "," + clone[2]);
    }
    private static boolean inRange(double val, double x) {
        double offset = 30;
        return x >= val - offset && x <= val + offset;

    }
}