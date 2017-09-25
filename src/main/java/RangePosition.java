import org.opencv.core.Mat;

public class RangePosition {
    private Position startPosition;
    private Position endPosition;

    public RangePosition() {
        this.startPosition = new Position();
        this.endPosition = new Position();
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }

    public Position getStartPosition() {

        return startPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public RangePosition compareStartAndSet(PositionType positionType, int val){
        return positionType.compareAndSet(this, val);
    }

    public Mat cutRange(Mat mat) {
       return mat.colRange(this.getStartPosition().getColNum(), this.getEndPosition().getColNum() + 1)
            .rowRange(this.getStartPosition().getRowNum(),this.getEndPosition().getRowNum() + 1)
        ;
    }

    public enum PositionType{
        START_ROW {
            RangePosition compareAndSet(RangePosition rangePosition, int val) {
                if (rangePosition.getStartPosition().getRowNum() == 0 || val < rangePosition.getStartPosition().getRowNum()) {
                    rangePosition.getStartPosition().setRowNum(val);
                }
                return rangePosition;
            }
        },END_ROW {
            RangePosition compareAndSet(RangePosition rangePosition, int val) {
                if (val > rangePosition.getEndPosition().getRowNum()) {
                    rangePosition.getEndPosition().setRowNum(val);
                }
                return rangePosition;
            }
        },START_COL {
            RangePosition compareAndSet(RangePosition rangePosition, int val) {
                if (rangePosition.getStartPosition().getColNum() == 0 || val < rangePosition.getStartPosition().getColNum()) {
                    rangePosition.getStartPosition().setColNum(val);
                }
                return rangePosition;
            }
        },END_COL {
            RangePosition compareAndSet(RangePosition rangePosition, int val) {
                if (val > rangePosition.getEndPosition().getColNum()) {
                    rangePosition.getEndPosition().setColNum(val);
                }
                return rangePosition;
            }
        };

        abstract RangePosition compareAndSet(RangePosition rangePosition,int val);
    }
}
